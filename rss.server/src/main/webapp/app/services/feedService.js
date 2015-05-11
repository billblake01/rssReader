app.service('feedService', function ($http, $resource) {


    this.getCategories = function () {
        var category = createCategoryResource();
        return category.query();
    };


    this.saveCategory = function (_category, callback) {
        if (typeof _category === "undefined") {
            return;
        }

        var Category = createCategoryResource();

//  Example of retrieving the resource first then updating it.
//
//            Category.get({categoryId : _category.categoryId}, function(returnedCategory) {
//                returnedCategory.name = _category.name;
//                returnedCategory.$save();
//            });

        var category = new Category({categoryId : _category.categoryId, name : _category.name});
        category.$save(callback);
    };


    function createCategoryResource() {
        return $resource(readerConstants.appContextPath + '/category/:categoryId',
                {categoryId : "@id"}
        );
    }


    this.getFeeds = function (_categoryId, _feedId, suc, fail, _page) {
        var feedResource = createFeedResource(_categoryId, _feedId, _page);
        return feedResource.query(suc, fail);
    };


    this.saveFeed = function(_feed, callback) {
        if (typeof _feed === "undefined") {
            return;
        }

        var Feed = createFeedResource(_feed.categoryId);

        var feed = new Feed({
            categoryId : _feed.categoryId,
            name : _feed.name,
            url : _feed.url,
            feedId : _feed.feedId,
            userName : _feed.userName
        });
        feed.$save(callback);
    };


    this.deleteFeed = function(_feed, callback) {
        if (typeof _feed === "undefined") {
            return;
        }

        var Feed = createFeedResource(_feed.categoryId, _feed.feedId);

        var feed = new Feed({
            categoryId : _feed.categoryId,
            name : _feed.name,
            url : _feed.url,
            feedId : _feed.feedId,
            userName : _feed.userName
        });
        feed.$delete(callback);
    };


    this.refreshFeeds = function (callback) {
        var feedResource = createFeedResource();
        return feedResource.refresh(callback);
    };


    function createFeedResource(_categoryId, _feedId, _page) {
        if (_feedId === null || typeof _feedId === "undefined") {
            _feedId = "@id";
        }
        if (_categoryId  === null || typeof _categoryId === "undefined") {
            _categoryId = "@id";
        }
        if (_page  === null || typeof _page === "undefined") {
            _page = "1";
        }
        var feedResource = $resource(readerConstants.appContextPath + '/feeds/category/:categoryId/feed/:feedId',
            {
                feedId : _feedId,
                categoryId : _categoryId,
                page : _page
            },
            {
                refresh : {method:'GET', isArray: true, params:{refresh:true}}
            }
        );
        return feedResource;
    };
});

