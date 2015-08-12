/**
 * Created by Franz on 11/08/2015.
 */
(function() {
  'use strict';
  angular.module('socialdumpApp.organizations')
    .controller('OrganizationDetailCtrl',
    ['$scope', 'OrganizationService', '$modalInstance', 'organizations', 'organization', 'index',
     function($scope, OrganizationService, $modalInstance, organizations,
              organization, index) {

       $scope.organization = angular.copy(organization);
       $scope.errorMessage = '';

       $scope.ok = function() {
         $modalInstance.close();
       };

       $scope.createOrg = function(organization) {
         OrganizationService.register(organization.name)
           .then(function(organization) {
             organizations.push(organization);
             $modalInstance.close();
           })
           .catch (function(error) {
             $scope.errorMessage = error;
         });
       };

       $scope.updateOrg = function(organization) {
         OrganizationService.update(organization)
           .then(function(organization) {
             organizations[index] = organization;
             $modalInstance.close();
           })
           .catch (function(error) {
             $scope.errorMessage = error;
         });
       };

       $scope.deleteOrg = function() {
         OrganizationService.delete($scope.organization.id)
           .then(function(data) {
             organizations.splice(index, 1);
             $modalInstance.close();
           })
           .catch (function(error) {
             $scope.errorMessage = error;
         });
       };

       $scope.cancel = function() {
         $modalInstance.dismiss('cancel');
       };

     }]
  );
})();
