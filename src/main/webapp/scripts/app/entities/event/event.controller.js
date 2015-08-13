(function() {
  'use strict';

  angular.module('socialdumpApp')
  .controller(
    'EventController', [
      '$scope', 'Event', 'EventStatus', 'EventType', 'ParseLinks',
      'OrganizationService', 'DateUtils',
      function($scope, Event, EventStatus, EventType, // SearchCriteria,
               ParseLinks, OrganizationService, DateUtils) {
        $scope.defaultDateTimeFormat = DateUtils.defaultDateTimeFormat();
        var dateTimeFormat = 'date: \'' + $scope.defaultDateTimeFormat + '\'';
        var buttonsTemplate =
          '<button type="submit"' +
          '    ui-sref="event.detail({id:id})"' +
          '    class="btn btn-info btn-sm">' +
          '  <span class="glyphicon glyphicon-eye-open"></span>' +
          '</button>' +
          '<button type="submit"' +
          '    ui-sref="event.edit({id:id})"' +
          '    class="btn btn-primary btn-sm">' +
          '  <span class="glyphicon glyphicon-pencil"></span>' +
          '</button>' +
          '<button type="submit"' +
          '    ng-click="delete(id)"' +
          '    class="btn btn-danger btn-sm">' +
          '  <span class="glyphicon glyphicon-remove-circle"></span>' +
          '</button>';
        $scope.events = [];
        $scope.page = 1;
        $scope.loadAll = function() {
          Event.query({page: $scope.page, per_page: 20,
                       organizationId: OrganizationService.getCurrentOrgId()},
            function(result, headers) {
              $scope.links = ParseLinks.parse(headers('link'));
              for (var i = 0; i < result.length; i++) {
                result[i].status = EventStatus.get({id: result[i].statusId});
                result[i].type = EventType.get({id: result[i].typeId});
                $scope.events.push(result[i]);
              }
            });
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
        $scope.loadAll();

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

        $scope.cancel = function(id) {
          Event.get({id: id}, function(result) {
            $scope.event = result;
            $('#cancelEventConfirmation').modal('show');
          });
        };

        $scope.confirmCancel = function(id) {
          Event.cancel({id: id},
            function() {
              $scope.reset();
              $('#cancelEventConfirmation').modal('hide');
              $scope.clear();
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
          // rowHeight: 36,
          enableColumnResizing: true,
          width: '*',
          data: $scope.events,
          columnDefs: [
            {
              // cellClass: 'col-sm-2',
              field: 'id',
              displayName: 'ID',
              visible: false
            },
            {
              // cellClass: 'col-sm-2',
              field: 'description',
              displayName: 'Descripción'
            },
            {
              // cellClass: 'col-sm-2',
              field: 'startDate',
              displayName: 'Comienza',
              cellFilter: dateTimeFormat
            },
            {
              // cellClass: 'col-sm-2',
              field: 'endDate',
              displayName: 'Termina',
              cellFilter: dateTimeFormat
            },
            // {
            //   // cellClass: 'col-sm-2',
            //   field: 'activatedAt',
            //   displayName: 'Activado el',
            //   cellFilter: dateTimeFormat
            // },
            {
              // cellClass: 'col-sm-1',
              field: 'status.status',
              displayName: 'Estado'
            },
            {
              // cellClass: 'col-sm-1',
              field: 'type.name',
              displayName: 'Tipo'
            },
            {
              // cellClass: 'col-sm-2',
              // field: 'icons',
              name: 'icons',
              displayName: '',
              enableHiding: false,
              enableSorting: false,
              // width: 80,
              cellTemplate: '<button type="submit"' +
                '    ui-sref="event.detail({id:row.entity[\'id\']})"' +
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
                '    ng-click="grid.appScope.cancel(row.entity[\'id\'])"' +
                '    class="btn btn-warning btn-sm">' +
                '  <i class="fa fa-toggle-on"></i>' +
                '</button>'
            }
          ]
        };
      }]);
}());
