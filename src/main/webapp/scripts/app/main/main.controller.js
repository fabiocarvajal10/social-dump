'use strict';

angular.module('socialdumpApp')
  .controller('MainController', function($scope, Principal, AuthServerProvider, $state) {
    Principal.identity().then(function(account) {
      $scope.account = account;
      $scope.isAuthenticated = Principal.isAuthenticated;
    });

    $scope.$watch('isAuthenticated', function(){
      if(!Principal.isAuthenticated() || !AuthServerProvider.hasValidToken()){
        AuthServerProvider.logout();
        $state.go('login');
      }
    });
  });
