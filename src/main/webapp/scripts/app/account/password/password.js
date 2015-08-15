'use strict';

angular.module('socialdumpApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('password', {
        parent: 'home',
        url: '/password',
        data: {
          roles: ['ROLE_USER'],
          pageTitle: 'Password'
        },
        views: {
          '': {
            templateUrl: 'scripts/app/account/password/password.html',
            controller: 'PasswordController'
          }
        },
        resolve: {}
      });
  });
