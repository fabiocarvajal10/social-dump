'use strict';

angular.module('socialdumpApp')
  .controller('EventDetailController', [
    '$scope', '$rootScope', '$stateParams', 'entity', 'Event', 'DateUtils',
    // '$state', '$window',
    function($scope, $rootScope, $stateParams, entity, Event, DateUtils) {
             // $state, $window) {
      // var tabFactory = function(heading, uiSref, active, select) {
      var tabFactory = function(heading, uiSref) {
        return {
          heading: heading,
          uiSref: uiSref
        };
      };

      $scope.event = entity;
      $scope.tabs = [];
      $scope.tabs.push(tabFactory('Reciente', 'event.detail.summary'));
      $scope.tabs.push(tabFactory('Detalles', 'event.detail.list'));
      $scope.tabs.push(tabFactory('Posts', 'public-posts({id: ' +
        $scope.event.id + '})'));
      $scope.defaultDateTimeFormat = DateUtils.defaultDateTimeFormat();
      $scope.load = function(id) {
        Event.getWithSummary({id: id}, function(result) {
          $scope.event = result;
        });
      };

      $rootScope.$on('socialdumpApp:eventUpdate', function(event, result) {
        $scope.event = result;
      });
    }]);
