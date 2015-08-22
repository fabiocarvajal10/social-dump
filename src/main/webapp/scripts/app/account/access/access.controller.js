/**
 * Created by Franz on 02/08/2015.
 */
'use strict';

angular.module('socialdumpApp.access')
  .controller('AccessController', function($rootScope, $scope, $state,
      AuthServerProvider, $location, localStorageService) {
    $scope.id = 0;
    $scope.event = 0;
    $scope.user = {};
    $scope.errors = {};

    $scope.errorMessage = '';

    $scope.noKeys = function() {
      return ($location.search().key === null || $location.search().key === undefined) ||
             ($location.search().id === null || $location.search().id === undefined)
    }

    $scope.login = function(event) {
      event.preventDefault();
      AuthServerProvider.access({
        id: $location.search().key,
        eventId: $location.search().id,
        username: $scope.username,
        password: $scope.password
      }).then(function() {
        localStorageService.set('tempAccessId', $location.search().key);
        $scope.authenticationError = false;
        $state.go('monitor-screen', {'id': $location.search().id});
      }).catch(function(error) {
        $scope.authenticationError = true;
        $scope.errorMessage = error;
      });
    };

  });
