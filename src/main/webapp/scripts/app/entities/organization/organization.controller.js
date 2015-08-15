/**
 * Created by Franz on 11/08/2015.
 */
(function() {
  'use strict';
  angular.module('socialdumpApp.organizations')
    .controller('OrganizationCtrl',
    ['$scope', 'OrganizationService', '$modal',
     function($scope, OrganizationService, $modal) {

       $scope.totalItems = 0;
       $scope.currentPage = 1;
       $scope.organizations = [];

       $scope.init = function() {
         OrganizationService.getAll($scope.currentPage, 12)
           .then(function(data) {
             $scope.organizations = data;
             $scope.totalItems = data.total;
           })
           .catch(function() {

           });
       };

       $scope.open = function(organization, index, action) {
         checkOrgsCant();
         var modalInstance = $modal.open({
           animation: true,
           templateUrl: getModalUrl(action),
           controller: 'OrganizationDetailCtrl',
           resolve: {
             'organizations': function() {
               return $scope.organizations;
             },
             'organization': function() {
               return organization;
             },
             'index': function() {
               return index;
             }
           }
         });

         modalInstance.result.then(function() {
           checkOrgsCant();
         }, function() {

         });
       };

       function checkOrgsCant() {
         if($scope.organizations.length === 0 && $scope.currentPage !== 1) {
           $scope.currentPage--;
           getOrganizations();
         } else if($scope.organizations.length > 12) {
           $scope.currentPage++;
           getOrganizations();
         }
       }

       $scope.init();

       $scope.pageChanged = function() {
         getOrganizations();
       };

       function getOrganizations() {
         OrganizationService.getAll($scope.currentPage, 12)
           .then(function(data) {
             $scope.organizations.splice(0, $scope.organizations.length);
             $scope.organizations = data;
             $scope.totalItems = data.total;
           })
           .catch(function() {

           });
       }

       function getModalUrl(action) {
         var baseUrl = 'scripts/app/entities/organization/partials/';
         var extension = '.html';
         return baseUrl + action + extension;
       }
     }
    ]
  );
})();
