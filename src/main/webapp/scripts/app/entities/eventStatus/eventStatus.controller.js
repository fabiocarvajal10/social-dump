'use strict';

angular.module('socialdumpApp')
  .controller('EventStatusController',
              function($scope, EventStatus, Event, ParseLinks) {
    $scope.eventStatuses = [];
    $scope.events = Event.query();
    $scope.page = 1;
    $scope.loadAll = function() {
      EventStatus.query({page: $scope.page, per_page: 20},
                        function(result, headers) {
        $scope.links = ParseLinks.parse(headers('link'));
        for (var i = 0; i < result.length; i++) {
          $scope.eventStatuses.push(result[i]);
        }
      });
    };
    $scope.reset = function() {
      $scope.page = 1;
      $scope.eventStatuses = [];
      $scope.loadAll();
    };
    $scope.loadPage = function(page) {
      $scope.page = page;
      $scope.loadAll();
    };
    $scope.loadAll();

    $scope.showUpdate = function(id) {
      EventStatus.get({id: id}, function(result) {
        $scope.eventStatus = result;
        $('#saveEventStatusModal').modal('show');
      });
    };

    $scope.save = function() {
      if ($scope.eventStatus.id !== null) {
        EventStatus.update($scope.eventStatus,
          function() {
            $scope.refresh();
          });
      } else {
        EventStatus.save($scope.eventStatus,
          function() {
            $scope.refresh();
          });
      }
    };

    $scope.delete = function(id) {
      EventStatus.get({id: id}, function(result) {
        $scope.eventStatus = result;
        $('#deleteEventStatusConfirmation').modal('show');
      });
    };

    $scope.confirmDelete = function(id) {
      EventStatus.delete({id: id},
        function() {
          $scope.reset();
          $('#deleteEventStatusConfirmation').modal('hide');
          $scope.clear();
        });
    };

    $scope.refresh = function() {
      $scope.reset();
      $('#saveEventStatusModal').modal('hide');
      $scope.clear();
    };

    $scope.clear = function() {
      $scope.eventStatus = {status: null, description: null, id: null};
      $scope.editForm.$setPristine();
      $scope.editForm.$setUntouched();
    };
  });
