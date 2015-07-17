'use strict';

angular.module('socialdumpApp')
  .controller('RegisterController', function($scope, $timeout, Auth) {
    $scope.success = null;
    $scope.error = null;
    $scope.doNotMatch = null;
    $scope.errorUserExists = null;
    $scope.registerAccount = {};
    $scope.msgState = 'Crear cuenta';

    $scope.validatePasswords = function() {
        return ($scope.registerAccount.password &&
                ($scope.registerAccount.password !== $scope.confirmPassword));
    };

    $scope.register = function() {

      if ($scope.registerAccount.password !== $scope.confirmPassword) {
        $scope.doNotMatch = 'ERROR';
      } else {
        $scope.registerAccount.langKey = 'en';
        $scope.doNotMatch = null;
        $scope.error = null;
        $scope.errorUserExists = null;
        $scope.errorEmailExists = null;

        Auth.createAccount($scope.registerAccount).then(function() {
          $scope.success = 'OK';
          $scope.msgState = 'Cuenta creada con éxito';
        }).catch (function(response) {
          $scope.success = null;
          if (response.status === 400 &&
              response.data === 'El nombre de usuario ya se encuentra en uso') {
            $scope.errorUserExists = 'ERROR';
          } else if (response.status === 400 &&
            response.data === 'El correo electrónico ya se encuentra en uso') {
            $scope.errorEmailExists = 'ERROR';
          } else {
            $scope.error = 'ERROR';
          }
        });

      }
    };
  });
