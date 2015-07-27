'use strict';

angular.module('socialdumpApp')
  .controller('EventDetailController', [
    '$scope', '$rootScope', '$stateParams', 'entity', 'Event', 'DateUtils',
    function($scope, $rootScope, $stateParams, entity, Event, DateUtils) {
      $scope.event = entity;
      $scope.defaultDateTimeFormat = DateUtils.defaultDateTimeFormat();
      $scope.load = function(id) {
        console.log('load event detail controller');
        Event.get({id: id}, function(result) {
          $scope.event = result;
        });
      };
      $rootScope.$on('socialdumpApp:eventUpdate', function(event, result) {
        $scope.event = result;
      });
    }]);
