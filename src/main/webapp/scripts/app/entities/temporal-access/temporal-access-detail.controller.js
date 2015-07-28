/**
 * Created by Franz on 27/07/2015.
 */
angular.module('socialdumpApp.temporalAccess')
  .controller('TemporalAccessDetailCtrl',
    function ($scope, TemporalAccessService, $modalInstance, MonitorService) {

      $scope.temporalAccess = {};
      $scope.monitorContacts = [];
      $scope.errorMessage = '';

      $scope.init = function(){
        MonitorService.getAll('')
          .then(function(data){
            $scope.monitorContacts = data;
            $scope.temporalAccess.allEvent = true;
          })
          .catch({

          });
      }

      $scope.createTA = function(){
        console.log($scope.temporalAccess);
      }

      $scope.ok = function () {
        $modalInstance.close();
      };

      $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
      };

      $scope.init();
  });
