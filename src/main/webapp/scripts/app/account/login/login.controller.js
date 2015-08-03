'use strict';

angular.module('socialdumpApp')
  .controller('LoginController', function($rootScope, $scope, $state, Auth) {
    $scope.user = {};
    $scope.errors = {};
    $scope.rememberMe = true;

    $scope.login = function(event) {
      event.preventDefault();
      Auth.login({
        username: $scope.username,
        password: $scope.password,
        rememberMe: $scope.rememberMe
      }).then(function() {
        $scope.authenticationError = false;
        console.log($rootScope.previousStateName);
        $state.go('dashboard');
      }).catch (function() {
        $scope.authenticationError = true;
      });
    };

  });
