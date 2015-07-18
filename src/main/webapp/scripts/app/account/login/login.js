(function() {
  'use strict';

  angular.module('socialdumpApp')
    .config(function($stateProvider) {
      $stateProvider
        .state('login', {
          parent: 'account',
          url: '/login',
          data: {
            roles: [],
            pageTitle: 'Iniciar Sesión'
          },
          views: {
            'content@': {
              templateUrl: 'scripts/app/account/login/login.html',
              controller: 'LoginController'
            }
          },
          resolve: {}
        });
    });
}());
