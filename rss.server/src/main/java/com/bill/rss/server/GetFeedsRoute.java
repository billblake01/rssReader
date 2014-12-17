package com.bill.rss.server;

import java.util.List;

import spark.Request;
import spark.Response;

import com.bill.rss.dataProvider.FeedItemProvider;
import com.bill.rss.dataProvider.FeedUpdater;
import com.bill.rss.domain.FeedItem;
import com.bill.rss.mongodb.FeedItemRetriever;
import com.bill.rss.mongodb.MongoFeedUpdater;

import static com.bill.rss.server.ViewConstants.CATEGORY_ID_PATH_VARIABLE;
import static com.bill.rss.server.ViewConstants.JSON_RESPONSE_TYPE;
import static com.bill.rss.server.ViewConstants.REFRESH_QUERY_PARAM;

public class GetFeedsRoute extends BaseRoute {


    private static final String FEED_ID_PATH_VARIABLE = ":feedId";

    private FeedItemProvider feedProvider;
    private FeedUpdater feedUpdater;

    protected GetFeedsRoute(String path) {
        super(path);
        feedProvider = new FeedItemRetriever();
        feedUpdater = new MongoFeedUpdater();
    }


    @Override
    public Object handle(Request request, Response response) {
        verifyUserLoggedIn(request, response);
        response.type(JSON_RESPONSE_TYPE);

        refreshFeedsIfSpecified(request);

        String categoryId = request.params(CATEGORY_ID_PATH_VARIABLE);
        String feedId = request.params(FEED_ID_PATH_VARIABLE);
        List<FeedItem> feedItems = feedProvider.retrieveFeedItems(categoryId, feedId, getUsername(request), getPage(request));
        return JsonUtils.convertObjectToJson(feedItems);
    }


    private int getPage(Request request) {
        try {
            return Integer.parseInt(request.queryParams("page"));
        } catch(Exception ex) {
            return 1;
        }
    }


    private void refreshFeedsIfSpecified(Request request) {
        String refresh = request.queryParams(REFRESH_QUERY_PARAM);
        if (refresh != null && Boolean.parseBoolean(refresh)) {
            feedUpdater.updateWithLatestFeeds(getUsername(request));
        }
    }


    public void setFeedProvider(FeedItemProvider feedProvider) {
        this.feedProvider = feedProvider;
    }

    public void setFeedUpdater(FeedUpdater feedUpdater) {
        this.feedUpdater = feedUpdater;
    }

}
