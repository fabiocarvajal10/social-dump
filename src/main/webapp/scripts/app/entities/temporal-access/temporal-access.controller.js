/**
 * Created by Franz on 27/07/2015.
 */
angular.module('socialdumpApp.temporalAccess')
  .controller('TemporalAccessController', ['$rootScope',
      '$scope', 'TemporalAccessService',
      '$modal', 'Event', 'DateUtils',
    function($rootScope, $scope, TemporalAccessService, 
        $modal, Event, DateUtils) {
      $scope.totalItems = 0;
      $scope.currentPage = 1;
      $scope.events = [];
      $scope.selectedEvent = {};
      $scope.gridTemporalAccesses = {};

      $scope.init = function() {
        //getGridData();
      };

      $rootScope
          .$on('currentOrganizationChange', function(event, args) {
            $scope.getEventOptions();
          });

      function buildMonitorNameField(data) {
        angular.forEach(data, function(row) {
          row.getNameAndLastName = function() {
            return row.monitorContactByMonitorContactId.firstName + ' ' +
                   row.monitorContactByMonitorContactId.lastName;
          };
        });
      }

      function buildGrid(data) {
        //ToDo mover a un gridTASettings factory o algo por el estilo
        var deleteCT = ' <button class="btn m-b-xs btn-xs btn-danger m-xs" ' +
                       ' ng-click="grid.appScope.open(row, \'delete\')"> ' +
                       ' <i class="fa fa-trash"></i></button> ';
        $scope.gridTemporalAccesses = {
          rowHeight: 36,
          width: '*',
          data: data,
          enableFiltering: true,
          columnDefs: [
            {field: 'getNameAndLastName()', displayName: 'Nombre del Monitor' },
            {
              field: 'monitorContactByMonitorContactId.email',
              displayName: 'Correo Electrónico'
            },
            {
              field: 'startDate',
              displayName: 'Fecha de Inicio',
              cellFilter: 'date:"dd/MM/yyyy hh:mma"'
            },
            {
              field: 'endDate',
              displayName: 'Fecha de Fin',
              cellFilter: 'date:"dd/MM/yyyy hh:mma"'
            },
            {
              field: 'icons',
              displayName: '',
              enableHiding: false,
              enableSorting: false,
              width: 80,
              cellTemplate: deleteCT
            }
          ]
        };
      }

      $scope.open = function(gridRow, action) {
        var modalInstance = $modal.open({
          animation: true,
          templateUrl: getModalUrl(action),
          controller: 'TemporalAccessDetailController',
          resolve: {
            'gridTA': function() {
              return $scope.gridTemporalAccesses;
            },
            'gridRow': function() {
              return gridRow;
            },
            'selectedEvent': function() {
              return $scope.selectedEvent;
            }
          }
        });

        modalInstance.result.then(function() {
          getGridData();
        }, function() {

        });
      };

      $scope.pageChanged = function() {
        getGridData();
      };

      function getGridData() {
        if (!isOrgEmpty()) {
          TemporalAccessService
            .getAllByEventId($scope.currentPage, 8, $scope.selectedEvent.id)
            .then(function(data) {
              buildGrid(data);
              buildMonitorNameField(data);
              $scope.totalItems = data.total;
              if ($scope.currentPage !== 1 && $scope.totalItems == 0) {
                $scope.currentPage--;
                getGridData();
              }
            })
            .catch(function(error) {

          });
        }
      };

      $scope.getEventOptions = function() {
        if (!isOrgEmpty()) {
          Event.query({organizationId: $rootScope.currentOrg.id},
            function(result) {
              $scope.events = result;
            });
        }
      };

      $scope.loadAccesses = function(item, model) {
        $scope.selectedEvent = item;
        getGridData();
      };

      $scope.checkAddState = function() {
        return ($scope.events.length === 0 ||
          $scope.isSelectedEventEmpty() || $scope.checkSelectedEventEndDate());
      };

      $scope.isSelectedEventEmpty = function() {
        return (Object.getOwnPropertyNames($scope.selectedEvent).length === 0);
      };

      $scope.checkSelectedEventEndDate = function() {
        return DateUtils.isDateLowerThanNow($scope.selectedEvent.endDate);
      };

      function getModalUrl(action) {
        var baseUrl = 'scripts/app/entities/temporal-access/partials/';
        var extension = '.html';
        return baseUrl + action + extension;
      };

      function isOrgEmpty() {
          return $rootScope.currentOrg === null ||
              $rootScope.currentOrg === undefined;
      };

    }
  ]);
