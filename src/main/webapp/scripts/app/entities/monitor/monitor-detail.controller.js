/**
 * Created by Franz on 25/07/2015.
 */
angular.module('socialdumpApp.monitors')
  .controller('MonitorDetailCtrl',
    function($scope, MonitorService, $modalInstance, monitorContacts,
      monitorContact, index) {

    $scope.monitorContact = angular.copy(monitorContact);
    $scope.errorMessage = '';
    $scope.onlyNumbers = /^\d+$/;

    $scope.ok = function() {
      $modalInstance.close();
    };

    $scope.createMC = function(monitorContact) {
      MonitorService.register(monitorContact)
      .then(function(monitorContact) {
        monitorContacts.push(monitorContact);
        $modalInstance.close();
      })
      .catch (function(error) {
        $scope.errorMessage = error;
      });
    };

    $scope.updateMC = function(monitorContact) {
      MonitorService.update(monitorContact)
      .then(function(monitorContact) {
        monitorContacts[index] = monitorContact;
        $modalInstance.close();
      })
      .catch (function(error) {
        $scope.errorMessage = error;
      });
    };

    $scope.deleteMC = function() {
      MonitorService.delete($scope.monitorContact.id)
      .then(function(data) {
        monitorContacts.splice(index, 1);
        $modalInstance.close();
      })
      .catch (function(error) {
        $scope.errorMessage = error;
      });
    };

    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    };
});
