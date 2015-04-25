app.controller('FeedManagerController', function($scope, feedService) {

//    $scope.name = getFullName();

    $scope.showTab = function($event) {
        $event.preventDefault();
        $($event.target).tab('show');
    };

    $scope.feeds = getFlatListOfFeeds();

    $scope.editFeed = function(feed, category) {
        $scope.currentFeed = feed;
        $scope.currentCategory = category;
    };

    $scope.deleteFeed = function(feed) {

    };


    $scope.editCategory = function(category) {
        $scope.currentCategory = $.extend({}, category);
    };

    $scope.deleteCategory = function(category) {

    };


    $scope.addFeed = function() {
        $scope.currentFeed = {};
    };

    $scope.addCategory = function() {
        $scope.currentCategory = {};
    };


    $scope.saveFeed = function(feed) {
        if (feed.categoryId === "new") {
            var category = {name : feed.newCategoryName};
            feedService.saveCategory(category, function(u, putResponseHeaders) {
                feed.categoryId = u.categoryId;
                feedService.saveFeed(feed);
            });
        } else {
            feedService.saveFeed(feed);
        }
    };


    function getFlatListOfFeeds() {
        feedService.getCategories().$then(function(response){
            var feedCategories = response.data;
            $scope.feedCategories = feedCategories;
            var feed = {}, feeds = [], cat;
          feedCategories.forEach(function(category) {
              cat = category;
              category.feeds.forEach(function(feedItem) {
                  feed.categoryId = cat.categoryId;
                  feed.category = cat.name;
                  feed.feedId = feedItem.feedId;
                  feed.name = feedItem.name;
                  feed.url = feedItem.url;
                  feeds.push(feed);
                  feed = {};
              });
          });
          $scope.feeds = feeds;
        });
    }

});