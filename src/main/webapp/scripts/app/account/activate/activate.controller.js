'use strict';

angular.module('socialdumpApp')
  .controller('ActivationController',
      function($scope, $stateParams, Auth, $state, $timeout) {
    Auth.activateAccount({key: $stateParams.key}).then(function() {
      $scope.error = null;
      $scope.success = 'OK';
      $scope.redirectToWithTO('login', 5000);
    }).catch (function() {
      $scope.success = null;
      $scope.error = 'ERROR';
      $scope.redirectToWithTO('register', 5000);
    });

    $scope.redirectToWithTO = function(state, time) {
      $timeout(function() {
        $state.go(state);
      }, time);
    };
  });

