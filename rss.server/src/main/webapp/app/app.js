﻿/// <reference path="../Scripts/angular-1.1.4.js" />

/*#######################################################################

  Dan Wahlin
  http://twitter.com/DanWahlin
  http://weblogs.asp.net/dwahlin
  http://pluralsight.com/training/Authors/Details/dan-wahlin

  Normally like to break AngularJS apps into the following folder structure
  at a minimum:

  /app
      /controllers
      /directives
      /services
      /partials
      /views

  #######################################################################*/

var app = angular.module('viewApp', ['ngResource', 'ngRoute', 'ngCookies', 'infinite-scroll', 'ngSanitize',
                                     'angularytics', 'angular-jqcloud']);

//This configures the routes and associates each route with a view and a controller
app.config(function ($routeProvider, AngularyticsProvider) {
    $routeProvider
        .when('/list',
            {
                controller: 'ListController',
                templateUrl: 'app/partials/list.jsp'
            })
        .when('/login',
            {
                controller: 'LoginController',
                templateUrl: 'app/partials/login.html'
            })
        .when('/signUp',
            {
                controller: 'SignUpController',
                templateUrl: 'app/partials/signUp.html'
            })
        .when('/manage',
            {
                controller: 'FeedManagerController',
                templateUrl: 'app/partials/manage.html'
            })
        .when('/logout',
            {
                templateUrl: 'app/partials/logout.html'
            })
        .otherwise({ redirectTo: '/list' });

    AngularyticsProvider.setEventHandlers(['Console', 'GoogleUniversal']);

}).run(function(Angularytics) {
    Angularytics.init();
});




