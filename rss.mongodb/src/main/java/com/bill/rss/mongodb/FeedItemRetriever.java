package com.bill.rss.mongodb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.bson.types.ObjectId;

import com.bill.rss.dataProvider.FeedItemProvider;
import com.bill.rss.dataProvider.FeedItemUpdater;
import com.bill.rss.domain.FeedItem;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import static com.bill.rss.mongodb.FeedConstants.CATEGORY_ID;
import static com.bill.rss.mongodb.FeedConstants.FEED_ID;
import static com.bill.rss.mongodb.FeedConstants.FEED_ITEMS;
import static com.bill.rss.mongodb.FeedConstants.FEED_ITEM_DESCRIPTION;
import static com.bill.rss.mongodb.FeedConstants.FEED_ITEM_LINK;
import static com.bill.rss.mongodb.FeedConstants.FEED_ITEM_OBJECT_ID;
import static com.bill.rss.mongodb.FeedConstants.FEED_ITEM_PUB_DATE;
import static com.bill.rss.mongodb.FeedConstants.FEED_ITEM_SOURCE;
import static com.bill.rss.mongodb.FeedConstants.FEED_ITEM_TITLE;
import static com.bill.rss.mongodb.FeedConstants.MAX_PAGE_SIZE;
import static com.bill.rss.mongodb.FeedConstants.USER_NAME;
import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class FeedItemRetriever implements FeedItemProvider, FeedItemUpdater {


    public List<FeedItem> retrieveFeedItems(String categoryId, String feedId, String username, int page) {
        DBCollection coll = getFeedItemsCollection();
        DBCursor feedItemsCursor;

        BasicDBObject query = buildFeedItemQuery(categoryId, feedId, username);
        feedItemsCursor = coll.find(query).sort(new BasicDBObject(FEED_ITEM_PUB_DATE, -1)).limit(MAX_PAGE_SIZE).skip((page - 1) * MAX_PAGE_SIZE);
        return parseFeadItems(feedItemsCursor);
    }


    public FeedItem markFeedItemAsRead(String feedItemId) {
        DBCollection feedItemCollection = getFeedItemsCollection();
        DBObject feedItem = retrieveFeedItemById(feedItemId);
        feedItem.put(FeedConstants.FEED_ITEM_READ, true);
        feedItemCollection.save(feedItem);
        return buildFeedItem(feedItem);
    }


    public FeedItem deleteFeedItem(String feedItemId) {
        DBObject feedItem = retrieveFeedItemById(feedItemId);
        DBCollection feedItemCollection = getFeedItemsCollection();
        feedItemCollection.remove(feedItem);
        return buildFeedItem(feedItem);
    }


    private DBCollection getFeedItemsCollection() {
        DB rssDb = MongoDBConnection.getDbConnection();
        DBCollection feedItemCollection = rssDb.getCollection(FEED_ITEMS);
        return feedItemCollection;
    }


    private DBObject retrieveFeedItemById(String feedItemId) {
        DBCollection feedItemCollection = getFeedItemsCollection();
        BasicDBObject query = new BasicDBObject();
        query.append(FEED_ITEM_OBJECT_ID, new ObjectId(feedItemId));
        DBObject feedItem = feedItemCollection.findOne(query);
        return feedItem;
    }


    private List<FeedItem> parseFeadItems(DBCursor feedItemsCursor) {
        List<FeedItem> feedItems = new ArrayList<FeedItem>();
        FeedItem feedItem;
        while (feedItemsCursor.hasNext()) {
            try {
                DBObject nextFeedItem = feedItemsCursor.next();
                feedItems.add(buildFeedItem(nextFeedItem));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return feedItems;
    }

    private FeedItem buildFeedItem(DBObject nextFeedItem) {
        FeedItem feedItem;
        feedItem = new FeedItem();
        feedItem.setFeedItemId(nextFeedItem.get(FEED_ITEM_OBJECT_ID).toString());
        feedItem.setCatId(nextFeedItem.get(CATEGORY_ID).toString());
        feedItem.setDescription(nextFeedItem.get(FEED_ITEM_DESCRIPTION).toString());
        feedItem.setFeedId(nextFeedItem.get(FEED_ID).toString());
        feedItem.setLink(nextFeedItem.get(FEED_ITEM_LINK).toString());
        feedItem.setSource(nextFeedItem.get(FEED_ITEM_SOURCE).toString());
        feedItem.setTitle(nextFeedItem.get(FEED_ITEM_TITLE).toString());
        feedItem.setUsername(nextFeedItem.get(USER_NAME).toString());
        feedItem.setRead((Boolean) nextFeedItem.get(FeedConstants.FEED_ITEM_READ));
        Date pubDate = (Date) nextFeedItem.get(FEED_ITEM_PUB_DATE);
        feedItem.setPubDate(pubDate);
        feedItem.setFormattedDate(formatDate(pubDate));
        return feedItem;
    }


    private BasicDBObject buildFeedItemQuery(String categoryId, String feedId, String username) {
        BasicDBObject query = new BasicDBObject();
        query.append(USER_NAME, username);
        if (categoryId != null) {
            query.append(CATEGORY_ID, new ObjectId(categoryId));
        }
        if (feedId != null) {
            query.append(FEED_ID, new ObjectId(feedId));
        }
        return query;
    }


    private String formatDate(Date pubDate) {
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTimeZone(TimeZone.getTimeZone("Europe/Dublin"));
        Calendar pubDateCalender = Calendar.getInstance();
        pubDateCalender.setTimeZone(TimeZone.getTimeZone("Europe/Dublin"));
        pubDateCalender.setTime(pubDate);

        if (nowCalendar.get(DATE) == pubDateCalender.get(DATE) &&
            nowCalendar.get(MONTH) == pubDateCalender.get(MONTH) &&
            nowCalendar.get(YEAR) == pubDateCalender.get(YEAR)) {

            SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
            timeFormatter.setTimeZone(TimeZone.getTimeZone("Europe/Dublin"));
            return timeFormatter.format(pubDate);
        } else {
            SimpleDateFormat time = new SimpleDateFormat("MMM dd");
            return time.format(pubDate);
        }
    }
}
