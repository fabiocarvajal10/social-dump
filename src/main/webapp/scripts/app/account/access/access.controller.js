/**
 * Created by Franz on 02/08/2015.
 */
'use strict';

angular.module('socialdumpApp.access')
  .controller('AccessController', function($rootScope, $scope, $state,
      AuthServerProvider, $location) {
    $scope.id = 0;
    $scope.user = {};
    $scope.errors = {};
    $scope.errorMessage = '';

    $scope.init = function() {
      $scope.id = $location.search().key;
    };

    $scope.login = function(event) {
      event.preventDefault();
      AuthServerProvider.access({
        id: $scope.id,
        username: $scope.username,
        password: $scope.password
      }).then(function() {
        $scope.authenticationError = false;
        //ToDo Pantalla monitor
        $state.go('home');
      }).catch (function(error) {
        $scope.authenticationError = true;
        $scope.errorMessage = error;
      });
    };

    $scope.init();
  });
