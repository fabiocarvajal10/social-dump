'use strict';

angular.module('socialdumpApp')
  .controller('AsideController', ['$rootScope', '$scope', '$location',
    'OrganizationService', 'localStorageService',
    function($rootScope, $scope, $location, OrganizationService,
             localStorageService) {
      $rootScope.currentOrg = null;
      $scope.orgz = [];
      $scope.org = $rootScope.currentOrg;

      function broadcastOrgChange() {
        $rootScope
          .$broadcast('currentOrganizationChange');
      }

      $scope.init = function() {
        OrganizationService.getAll(null, 15).then(function(data) {
          $scope.orgz = data;
          $rootScope.currentOrg = localStorageService.get('currentOrg') ||
                                  data[0];
          $scope.org = $rootScope.currentOrg;
          broadcastOrgChange();
        });
      };

      $scope.onChange = function(id) {
        var tmp = null;
        $scope.orgz.forEach(function(element, index, array) {
          if (element.id === id) {
            tmp = $scope.currentOrg;
            $rootScope.currentOrg = element;
            broadcastOrgChange();
          }
        });
      };

      $rootScope.$on('newOrganization', function(event, args) {
        var newOrg = args.newOrganization;
        $scope.orgz.push(newOrg);
        $rootScope.currentOrg = newOrg;
        $scope.org = newOrg;
      });

      $rootScope.$on('updatedOrganization', function(event, args) {
        var updatedOrg = args.updatedOrganization;
        $scope.orgz.forEach(function(element, index, array) {
          if (element.id === updatedOrg.id) array[index] = updatedOrg;
        });
        if ($rootScope.currentOrg.id === updatedOrg.id) {
          $rootScope.currentOrg = updatedOrg;
        }
      });

      $rootScope.$on('deletedOrganization', function(event, args) {
        var deletedOrg = args.deletedOrganization;
        $scope.orgz.forEach(function(element, index, array) {
          if (element.id === deletedOrg) array.splice(index, 1);
        });
        if ($rootScope.currentOrg.id === deletedOrg) {
          if (!!$scope.orgz && $scope.orgz.length > 0) {
            $rootScope.currentOrg = $scope.orgz[0];
          } else {
            $rootScope.currentOrg = null;
            $scope.org = null;
          }
        }
      });

      $scope.init();
    }
  ]);
