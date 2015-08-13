'use strict';

angular.module('socialdumpApp')
  .controller('AsideController', ['$scope', '$location', '$state',
    'Auth', 'Principal', 'OrganizationService',
    function($scope, $location, $state, Auth, Principal, OrganizationService) {
      $scope.isAuthenticated = Principal.isAuthenticated;
      $scope.$state = $state;

      $scope.orgz = [];

      $scope.init = function() {
        console.log('fuck');
        OrganizationService.getAll(null, 15).then(function(data) {
          $scope.orgz = data;
          console.log(data);
        });
      };
      console.log('fuck');
      $scope.init();
    }
  ]);
