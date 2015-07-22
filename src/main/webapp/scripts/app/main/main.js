'use strict';

angular.module('socialdumpApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('home', {
        parent: 'site',
        url: '/',
        data: {
          roles: []
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/main/app.html',
            controller: 'MainController'
          }
        },
        resolve: {
          authorize: ['Auth',
            function(Auth) {
              return Auth.authorize();
            }
          ]
        }
      });
  });
