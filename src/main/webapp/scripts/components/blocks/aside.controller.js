'use strict';

angular.module('socialdumpApp')
  .controller('AsideController', ['$scope', '$location',
    'OrganizationService',
    function($scope, $location, OrganizationService) {
      $scope.currentOrg = null;
      $scope.orgz = [];

      $scope.init = function() {
        OrganizationService.getAll(null, 15).then(function(data) {
          $scope.orgz = data;
          $scope.currentOrg = data[0];
        });
      };

      $scope.change = function(index) {
        $scope.currentOrg = data[index];
      };

      $scope.$on('newOrganization', function(event, args) {
        var newOrg = args.newOrganization;
        $scope.orgz.push(newOrg);
      });

      $scope.$on('updatedOrganization', function(event, args) {
        var updatedOrg = args.updatedOrganization;
        $scope.orgz.forEach(function(element, index, array) {
          if (element.id === updatedOrg.id) array[index] = updatedOrg;
        });
        if ($scope.currentOrg.id === updatedOrg.id) {
          $scope.currentOrg = updatedOrg;
        }
      });

      $scope.$on('deletedOrganization', function(event, args) {
        var deletedOrg = args.deletedOrganization;
        $scope.orgz.forEach(function(element, index, array) {
          if (element.id === deletedOrg) array.splice(index, 1);
        });
        if ($scope.currentOrg.id === deletedOrg) {
          $scope.currentOrg = $scope.orgz[0];
        }
      });

      $scope.init();
    }
  ]);
