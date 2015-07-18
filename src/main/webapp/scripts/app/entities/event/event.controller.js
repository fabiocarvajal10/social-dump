'use strict';

angular.module('socialdumpApp')
.controller('EventController',
  function($scope, Event, //Organization, EventStatus, EventType,
           // SearchCriteria, TemporalAccess,
           ParseLinks) {
    $scope.events = [];
/*    $scope.organizations = Organization.query();
    $scope.eventstatuss = EventStatus.query();
    $scope.eventtypes = EventType.query();
    $scope.searchcriterias = SearchCriteria.query();
    $scope.temporalaccesss = TemporalAccess.query();*/
    $scope.page = 1;
    $scope.loadAll = function() {
      Event.query({page: $scope.page, per_page: 20},
        function(result, headers) {
          $scope.links = ParseLinks.parse(headers('link'));
          for (var i = 0; i < result.length; i++) {
            $scope.events.push(result[i]);
          }
        });
    };
    $scope.reset = function() {
      $scope.page = 1;
      $scope.events = [];
      $scope.loadAll();
    };
    $scope.loadPage = function(page) {
      $scope.page = page;
      $scope.loadAll();
    };
    $scope.loadAll();

    $scope.showUpdate = function(id) {
      Event.get({id: id}, function(result) {
        $scope.event = result;
        $('#saveEventModal').modal('show');
      });
    };

    $scope.save = function() {
      if ($scope.event.id != null) {
        Event.update($scope.event,
          function() {
            $scope.refresh();
          });
      } else {
        Event.save($scope.event,
          function() {
            $scope.refresh();
          });
      }
    };

    $scope.delete = function(id) {
      Event.get({id: id}, function(result) {
        $scope.event = result;
        $('#deleteEventConfirmation').modal('show');
      });
    };

    $scope.confirmDelete = function(id) {
      Event.delete({id: id},
        function() {
          $scope.reset();
          $('#deleteEventConfirmation').modal('hide');
          $scope.clear();
        });
    };

    $scope.refresh = function() {
      $scope.reset();
      $('#saveEventModal').modal('hide');
      $scope.clear();
    };

    $scope.clear = function() {
      $scope.event = {startDate: null, endDate: null, description: null,
                      activatedAt: null, postDelay: null, id: null};
        $scope.editForm.$setPristine();
        $scope.editForm.$setUntouched();
      };
    });
