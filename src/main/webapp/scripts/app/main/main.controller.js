'use strict';

angular.module('socialdumpApp')
  .controller('MainController',
    function($scope, Principal, AuthServerProvider, $state, $modalStack) {
    Principal.identity().then(function(account) {
      $scope.account = account;
      $scope.isAuthenticated = Principal.isAuthenticated;
      $scope.hasValidToken = AuthServerProvider.hasValidToken();
    });

    $scope.$watch(function(){
      return AuthServerProvider.hasValidToken();
    }, function(){
      if(!Principal.isAuthenticated() || !AuthServerProvider.hasValidToken()){
        $modalStack.dismissAll('cancel');
        AuthServerProvider.logout();
        $state.go('login');
      }
    });
  });
