(function() {
  'use strict';

  angular.module('socialdumpApp')
    .controller('EventTypeController',
          function($scope, EventType, Event, ParseLinks) {
      $scope.eventTypes = [];
      $scope.events = Event.query();
      $scope.page = 1;
      $scope.loadAll = function() {
        EventType.query({page: $scope.page, per_page: 20},
                         function(result, headers) {
          $scope.links = ParseLinks.parse(headers('link'));
          for (var i = 0; i < result.length; i++) {
            $scope.eventTypes.push(result[i]);
          }
        });
      };
      $scope.reset = function() {
        $scope.page = 1;
        $scope.eventTypes = [];
        $scope.loadAll();
      };
      $scope.loadPage = function(page) {
        $scope.page = page;
        $scope.loadAll();
      };
      $scope.loadAll();

      $scope.showUpdate = function(id) {
        EventType.get({id: id}, function(result) {
          $scope.eventType = result;
          $('#saveEventTypeModal').modal('show');
        });
      };

      $scope.save = function() {
        if ($scope.eventType.id != null) {
          EventType.update($scope.eventType,
            function() {
              $scope.refresh();
            });
        } else {
          EventType.save($scope.eventType,
            function() {
              $scope.refresh();
            });
        }
      };

      $scope.delete = function(id) {
        EventType.get({id: id}, function(result) {
          $scope.eventType = result;
          $('#deleteEventTypeConfirmation').modal('show');
        });
      };

      $scope.confirmDelete = function(id) {
        EventType.delete({id: id},
          function() {
            $scope.reset();
            $('#deleteEventTypeConfirmation').modal('hide');
            $scope.clear();
          });
      };

      $scope.refresh = function() {
        $scope.reset();
        $('#saveEventTypeModal').modal('hide');
        $scope.clear();
      };

      $scope.clear = function() {
        $scope.eventType = {name: null, description: null, id: null};
        $scope.editForm.$setPristine();
        $scope.editForm.$setUntouched();
      };
    });
}());
