/**
 * Created by Franz on 27/07/2015.
 */
angular.module('socialdumpApp.temporalAccess')
  .controller('TemporalAccessCtrl',
    function($scope, TemporalAccessService, $modal) {

    $scope.gridTemporalAccesses = {};

    $scope.init = function() {
      TemporalAccessService.getAll('e')
        .then(function(data) {
          if(data.length > 0){
            buildGrid(data);
            buildMonitorNameField(data);
          }
        })
        .catch(function(error){

        });
    }

    function buildMonitorNameField(data){
      angular.forEach(data, function(row){
        row.getNameAndLastName = function(){
          return row.monitorContactByMonitorContactId.firstName + ' ' +
                 row.monitorContactByMonitorContactId.lastName;
        }
      });
    }

    function buildGrid(data){
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
          {field: 'monitorContactByMonitorContactId.email', displayName: 'Correo Electrónico'},
          {field: 'startDate', displayName: 'Fecha de Inicio', cellFilter: 'date:"dd/MM/yyyy hh:mma"' },
          {field: 'endDate', displayName: 'Fecha de Fin', cellFilter: 'date:"dd/MM/yyyy hh:mma"' },
          {
            field: 'icons',
            displayName: '',
            enableHiding: false,
            enableSorting: false,
            width: 80,
            cellTemplate: deleteCT
          }
        ]
      }
    }

    $scope.open = function (gridRow, action) {
      var modalInstance = $modal.open({
        animation: true,
        templateUrl: getModalUrl(action),
        controller: 'TemporalAccessDetailCtrl',
        resolve: {
          'gridTA': function(){
            return $scope.gridTemporalAccesses;
          },
          'gridRow': function(){
            return gridRow;
          }
        }
      });

      modalInstance.result.then(function() {
        $scope.init();
      }, function () {

      });
    };

    $scope.test = function(monitor){
      console.log(monitor);
    }

    $scope.init();

    function getModalUrl(action) {
      var baseUrl = 'scripts/app/entities/temporal-access/partials/';
      var extension = '.html';
      return baseUrl + action + extension;
    }
  });
