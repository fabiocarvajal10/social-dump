'use strict';

angular.module('socialdumpApp')
  .controller('SettingsController', ['$scope', 'Principal', 'Auth', '$modal',
      function ($scope, Principal, Auth, $modal) {
    $scope.success = null;
    $scope.error = null;
    Principal.identity(true).then(function(account) {
      $scope.settingsAccount = account;
    });

    $scope.save = function () {
      Auth.updateAccount($scope.settingsAccount).then(function() {
        $scope.error = null;
        $scope.errorEmailExists = false;
        $scope.success = 'OK';
        Principal.identity().then(function(account) {
            //$scope.settingsAccount = account;
        });
      }).catch(function(error) {
        if(error.data.exception
           === 'org.springframework.dao.DataIntegrityViolationException') {
          $scope.errorEmailExists = true;
        } else {
          $scope.success = null;
          $scope.error = 'ERROR';
        }
      });
    };

    $scope.open = function() {
      var modalInstance = $modal.open({
        animation: true,
        templateUrl: 'scripts/app/account/settings/partials/delete.html',
        controller: 'SettingsDetailController',
        resolve: {}
      });

      modalInstance.result.then(function() {

      }
      ,function() {

      });
    };

  }]);
