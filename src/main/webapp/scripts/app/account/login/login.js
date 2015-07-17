'use strict';

angular.module('socialdumpApp')
  .config(function($stateProvider) {
    $stateProvider
        .state('login', {
          url: '/login',
          data: {
            roles: [],
            pageTitle: 'Iniciar Sesión'
          },
          templateUrl: 'scripts/app/account/login/login.html',
          controller: 'LoginController',
          resolve: {}
        });
  });
