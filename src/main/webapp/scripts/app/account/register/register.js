'use strict';

angular.module('socialdumpApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('register', {
        url: '/register',
        data: {
          roles: [],
          pageTitle: 'Crear Cuenta'
        },
        templateUrl: 'scripts/app/account/register/register.html',
        controller: 'RegisterController'
    });
  });
