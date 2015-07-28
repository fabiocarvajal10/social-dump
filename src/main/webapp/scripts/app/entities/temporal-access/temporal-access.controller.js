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
          }
        })
        .catch(function(error){

        });
    }

    function buildGrid(data){
      $scope.gridTemporalAccesses = {
        rowHeight: 36,
        width: '*',
        data: data,
        enableFiltering: true,
        columnDefs: [
          {field: 'monitorContactByMonitorContactId.firstName', displayName: 'Monitor' },
          {field: 'email', displayName: 'Correo Electrónico'},
          {field: 'startDate', displayName: 'Fecha de Inicio', cellFilter: 'date:"dd/MM/yyyy hh:mma"' },
          {field: 'endDate', displayName: 'Fecha de Fin', cellFilter: 'date:"dd/MM/yyyy hh:mma"' },
          {
            field: 'icons',
            displayName: '',
            enableHiding: false,
            enableSorting: false,
            width: 80,
            cellTemplate: '<button class="btn m-b-xs btn-xs btn-danger m-xs" ng-click="test()"><i class="fa fa-trash"></i></button>'
          }
        ]
      }
    }

    $scope.open = function (temporalAccess, index, action) {

      var modalInstance = $modal.open({
        animation: true,
        templateUrl: getModalUrl(action),
        controller: 'TemporalAccessDetailCtrl',
        resolve: {}
      });

      modalInstance.result.then(function() {

      }, function () {

      });
    };

    $scope.test = function() {
      console.log('Test');
    }
    $scope.init();

    function getModalUrl(action) {
      var baseUrl = 'scripts/app/entities/temporal-access/partials/';
      var extension = '.html';
      return baseUrl + action + extension;
    }
  });
