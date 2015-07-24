/**
 * Created by Franz on 23/07/2015.
 */
angular.module('socialdumpApp')
  .controller('MonitorCtrl',
    function($scope, MonitorService) {
      $scope.monitorContacts = [];

      $scope.init = function(){
        MonitorService.getAll('foo')
          .then(function(data) {
            $scope.monitorContacts = data;
          })
          .catch(function() {

          });
      }

      $scope.deleteMonitor = function(monitor, index){
        $scope.monitorContacts.splice(index, 1);
      }

      $scope.init();
    });

