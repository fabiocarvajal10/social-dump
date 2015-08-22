'use strict';

angular.module('socialdumpApp')
  .controller('NavbarController',
    function($scope, $location, $state, Auth, Principal, localStorageService) {
      $scope.isAuthenticated = Principal.isAuthenticated;
      $scope.$state = $state;

      $scope.userFirstName = localStorageService.get('loggedUser').firstName;

      $scope.$on('accountUpdated', function(){
        $scope.userFirstName = localStorageService.get('loggedUser').firstName;
      });

      $scope.logout = function() {
        Auth.logout();
        $state.go('home');
      };
    }
  );
