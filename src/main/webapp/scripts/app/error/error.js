'use strict';

angular.module('socialdumpApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('error', {
        parent: 'site',
        url: '/error',
        data: {
          roles: [],
          pageTitle: 'Error page!'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/error/error.html'
          }
        },
        resolve: { }
      })
      .state('accessdenied', {
        parent: 'site',
        url: '/accessdenied',
        data: {
          roles: []
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/error/accessdenied.html'
          }
        },
        resolve: {}
      })
      .state('404', {
        parent: 'site',
        url: '/404',
        data: {
          roles: [],
          pageTitle: 'Not Found!'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/error/notfound.html'
          }
        },
        resolve: {}
      });
  });
