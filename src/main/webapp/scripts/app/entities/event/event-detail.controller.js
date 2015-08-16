'use strict';

angular.module('socialdumpApp')
  .controller('EventDetailController', [
    '$scope', '$rootScope', '$stateParams', 'entity', 'Event', 'DateUtils',
    function($scope, $rootScope, $stateParams, entity, Event, DateUtils) {
      var tabFactory = function(heading, uiSref) {
        return {
          heading: heading,
          uiSref: uiSref
        };
      };

      $scope.colors = [];
      for (var color in $scope.app.color) {
        if (!$scope.app.color.hasOwnProperty(color)) {
          continue;
        }
        $scope.colors.push($scope.app.color[color]);
      }

      $scope.pieChartOptions = {
        type: 'pie', height: 126, sliceColors: $scope.colors};

      $scope.event = entity;
      $scope.tabs = [];
      $scope.tabs.push(tabFactory('Reciente', 'event.detail.summary'));
      $scope.tabs.push(tabFactory('Detalles', 'event.detail.list'));
      $scope.tabs.push(tabFactory('Posts', 'public-posts({id: event.id})'));
      $scope.defaultDateTimeFormat = DateUtils.defaultDateTimeFormat();
      $scope.load = function(id) {
        var success = function(result) {
          $scope.event = result;
        };
        var failure = function(result) {
          console.log('fail');
        };
        Event.getWithSummary({'id': id}, success, failure);
      };

      $rootScope.$on('socialdumpApp:eventUpdate', function(event, result) {
        $scope.event = result;
      });
    }]);
