'use strict';

angular.module('socialdumpApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('register', {
        parent: 'account',
        url: '/register',
        data: {
          roles: [],
          pageTitle: 'Crear Cuenta'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/account/register/register.html',
            controller: 'RegisterController'
          }
        },
        resolve: {}
    });
  });
