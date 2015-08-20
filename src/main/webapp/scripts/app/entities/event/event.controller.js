(function() {
  'use strict';

  angular.module('socialdumpApp')
  .controller(
    'EventController', [
      '$rootScope', '$scope', 'Event', 'EventStatus', 'EventType',
      'ParseLinks', 'DateUtils',
      function($rootScope, $scope, Event, EventStatus, EventType,
          ParseLinks, DateUtils) {
        $scope.defaultDateTimeFormat = DateUtils.defaultDateTimeFormat();
        var dateTimeFormat = 'date: \'' + $scope.defaultDateTimeFormat + '\'';
        $scope.events = [];
        $scope.page = 1;

        $rootScope
          .$on('currentOrganizationChange', function(event, args) {
            $scope.reset();
          });

        $rootScope
          .$on('socialdumpApp:eventAdded', function(event, data) {
            data.status = EventStatus.get({id: data.statusId});
            data.type = EventType.get({id: data.typeId});
            var index = findOnList(data.id);
            if (index === -1) {
              $scope.events.push(data);
            } else {
              $scope.events[index] = data;
            }
          });

        function findOnList(eventId) {
          var index = -1;
          $scope.events.forEach(function(element, i, array) {
            if (element.id === eventId) return index = i;
          });
          return index;
        }

        $scope.loadAll = function() {
          if (!isOrgEmpty()) {
            Event.query({page: $scope.page, per_page: 20,
                         organizationId: $rootScope.currentOrg.id},
              function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                  result[i].status = EventStatus.get({id: result[i].statusId});
                  result[i].type = EventType.get({id: result[i].typeId});
                  $scope.events.push(result[i]);
                }
              });
          }
        };

        $scope.reset = function() {
          $scope.page = 1;
          $scope.events = [];
          $scope.loadAll();
          $scope.gridOptions.data = $scope.events;
        };

        $scope.loadPage = function(page) {
          $scope.page = page;
          $scope.loadAll();
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

        $scope.cancelEvent = function(id) {
          Event.get({id: id}, function(result) {
            $scope.errorMessage = '';
            $scope.event = result;
            $('#cancelEventConfirmation').modal('show');
          });
        };

        $scope.confirmCancelEvent = function(id) {
          Event.cancel({id: id})
            .$promise.then(function() {
              $scope.reset();
              $('#cancelEventConfirmation').modal('hide');
              $scope.clear();
            }, function(error) {
              $scope.errorMessage = error;
            });
        };

        $scope.refresh = function() {
          $scope.reset();
          $scope.clear();
        };

        $scope.clear = function() {
          $scope.event = {startDate: null, endDate: null, description: null,
                          activatedAt: null, postDelay: null, id: null};
        };

        $scope.gridOptions = {
          enableColumnResizing: true,
          width: '*',
          data: $scope.events,
          columnDefs: [
            {
              field: 'id',
              displayName: 'ID',
              visible: false
            },
            {
              field: 'description',
              displayName: 'Descripción'
            },
            {
              field: 'startDate',
              displayName: 'Comienza',
              cellFilter: dateTimeFormat
            },
            {
              field: 'endDate',
              displayName: 'Termina',
              cellFilter: dateTimeFormat
            },
            {
              field: 'status.status',
              displayName: 'Estado'
            },
            {
              field: 'type.name',
              displayName: 'Tipo'
            },
            {
              name: 'icons',
              displayName: '',
              enableHiding: false,
              enableSorting: false,
              cellTemplate:
                '<button type="submit"' +
                '    ui-sref="event.detail.summary({id:row.entity[\'id\']})"' +
                '    class="btn btn-info btn-sm">' +
                '  <span class="glyphicon glyphicon-eye-open"></span>' +
                '</button>' +
                '<button type="submit"' +
                '    ui-sref="event.edit({id:row.entity[\'id\']})"' +
                '    class="btn btn-primary btn-sm">' +
                '  <span class="glyphicon glyphicon-pencil"></span>' +
                '</button>' +
                '<button type="submit"' +
                '    ng-click="grid.appScope.delete(row.entity[\'id\'])"' +
                '    class="btn btn-danger btn-sm">' +
                '  <span class="glyphicon glyphicon-remove-circle"></span>' +
                '</button>' +
                '<button type="submit"' +
                '    ng-click="grid.appScope.cancelEvent(row.entity[\'id\'])"' +
                '    class="btn btn-warning btn-sm">' +
                '  <i class="fa fa-toggle-on"></i>' +
                '</button>'
            }
          ]
        };

        function isOrgEmpty() {
            return $rootScope.currentOrg === null ||
                $rootScope.currentOrg === undefined;
        };

        $scope.loadAll();
      }]);
}());
