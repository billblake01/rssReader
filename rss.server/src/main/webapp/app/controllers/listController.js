app.controller('ListController', function($scope, feedService, $cookies, $cookieStore, $location, $rootScope, $http, userService) {

    var loggedInValue = $cookies.loggedIn;
    if (loggedInValue !== "logged-in" && !$rootScope.loggedIn) {
        $location.path('/login');
        return;
    }

    $scope.page = 1;
    $scope.loading = true;
    $scope.loadingMessage = "Loading Feeds";
    $scope.feedCategories = feedService.getCategories();
    $scope.feeds = feedService.getFeeds(null, null, loadFeedsSuccessful, fail);
    $scope.name = userService.getFullName();
    $scope.title = "All Feeds";

    $scope.loadMore = function() {
        $scope.page++;
        $scope.loading = true;
        feedService.getFeeds($scope.categoryId, $scope.feedId, loadMoreFeedsSuccessful, fail, $scope.page);
    };

    $scope.displayFeedsForCategory = function(category) {
        $scope.loading = true;
        $scope.categoryId = category.categoryId;
        $scope.feedId = undefined;
        $scope.feeds = feedService.getFeeds(category.categoryId, $scope.feedId, loadFeedsSuccessful, fail);
        $scope.title = category.name;
    };

    $scope.displayFeedsForAllCategory = function() {
        $scope.loading = true;
        $scope.feeds = {};
        $scope.categoryId = undefined;
        $scope.feedId = undefined;
        $scope.feeds = feedService.getFeeds(null, null, loadFeedsSuccessful, fail);
        $scope.title = "All Feeds";
    };

    $scope.displayFeedsForFeed = function(feed) {
        $scope.loading = true;
        $scope.categoryId = undefined;
        $scope.feedId = feed.feedId;
        $scope.feeds = feedService.getFeeds($scope.categoryId, feed.feedId, loadFeedsSuccessful, fail);
        $scope.title = feed.name;
    };

    $scope.refresh = function() {
        feedService.refreshFeeds(showRefreshedFeeds);
    };

    $scope.articleClass = function(index) {
        return "article-" + index;
    };

    $scope.toggleArticle = function(index) {
        $(".article-" + index + ":first").toggle();
    };

    $scope.logout = function() {
        var responsePromise = $http.get(readerConstants.appContextPath + "/logout");

        responsePromise.success(function(user, status, headers, config) {
            $location.path('/logout');
            $cookies.loggedIn = "logged-out";
            $rootScope.loggedIn = false;
            $cookieStore.remove("user");
        });
    };


    $scope.toggleSideBar = function() {
        $scope.sideBarClass = ($scope.sideBarClass !== "display") ? "display" : "";
    };


    function showRefreshedFeeds() {
        $scope.feeds = feedService.getFeeds();
    }


    function loadFeedsSuccessful(data) {
        $scope.loading = false;
    };

    function loadMoreFeedsSuccessful(newlyFetchedFeeds) {
        $scope.feeds = $scope.feeds.concat(newlyFetchedFeeds);
        $scope.loading = false;
    }

    function fail() {
    };
});