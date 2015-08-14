/**
 * Created by Franz on 11/08/2015.
 */
angular.module('socialdumpApp')
  .controller('SettingsDetailController', ['$scope', 'Principal', 'Auth',
    '$modalInstance', 'AccountService', '$state',
      function ($scope, Principal, Auth, $modalInstance, AccountService, $state) {
    $scope.errorMessage = '';
    $scope.delete = function(password) {
      console.log(password);
    };

    $scope.delete = function(password) {
      AccountService.delete(password)
      .then(function(data) {
        Auth.logout();
        $state.go('login');
      })
      .catch(function(error) {
        $scope.errorMessage = error;
      });
    };

    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    };

  }]);
