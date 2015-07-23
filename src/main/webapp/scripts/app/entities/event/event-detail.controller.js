'use strict';

angular.module('socialdumpApp')
  .controller(
    'EventDetailController', [
    '$scope', '$rootScope', '$stateParams', 'entity', 'Event', 'Organization',
    'EventStatus', 'EventType', 'SearchCriteria',// 'TemporalAccess',
    function($scope, $rootScope, $stateParams, entity, Event) {//,
             // Organization, EventStatus, EventType, SearchCriteria,
             //TemporalAccess
      $scope.event = entity;
      $scope.load = function(id) {
        Event.get({id: id}, function(result) {
          $scope.event = result;
        });
      };
      $rootScope.$on('socialdumpApp:eventUpdate', function(event, result) {
        $scope.event = result;
      });
    }]);
