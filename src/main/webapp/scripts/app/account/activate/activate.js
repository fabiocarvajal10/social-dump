'use strict';

angular.module('socialdumpApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('activate', {
        parent: 'account',
        url: '/activate?key',
        data: {
          roles: [],
          pageTitle: 'Activación de Cuenta'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/account/activate/activate.html',
            controller: 'ActivationController'
          }
        },
        resolve: {}
      });
  });

