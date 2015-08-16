'use strict';

angular.module('socialdumpApp')
  .controller('ResetFinishController', ['$scope', '$stateParams', '$timeout', 'Auth',
    function ($scope, $stateParams, $timeout, Auth) {

      $scope.keyMissing = $stateParams.key === undefined;
      $scope.doNotMatch = null;
      $scope.resetAccount = {};

      $scope.validatePasswords = function() {
        return ($scope.resetAccount.password &&
                ($scope.resetAccount.password !== $scope.confirmPassword));
      };

      $scope.finishReset = function() {
        if ($scope.resetAccount.password !== $scope.confirmPassword) {
          $scope.doNotMatch = 'ERROR';
        } else {
          Auth.resetPasswordFinish({key: $stateParams.key, newPassword: $scope.resetAccount.password}).then(function () {
          $scope.success = 'OK';
        }).catch(function (response) {
          $scope.success = null;
          $scope.error = 'ERROR';

          });
        }

      };
  }]);
