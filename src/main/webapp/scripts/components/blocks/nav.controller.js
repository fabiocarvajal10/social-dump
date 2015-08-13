'use strict';

angular.module('socialdumpApp')
  .controller('NavbarController',
    function($scope, $location, $state, Auth, Principal) {
      $scope.isAuthenticated = Principal.isAuthenticated;
      $scope.$state = $state;
      console.log('adsd')
      $scope.logout = function() {
        Auth.logout();
        $state.go('home');
      };
    }
  );
