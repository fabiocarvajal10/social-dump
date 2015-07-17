/**
 * Created by Franz on 17/07/2015.
 */
angular.module('socialdumpApp')
  .controller('OrganizationCtrl', function($scope, OrganizationService, localStorageService) {
    $scope.organizations = [];

    $scope.init = function() {
      OrganizationService.getAll()
        .then(function(data) {
          if (data.length !== 0) {
            $scope.organizations = data;
        } else {
            //$scope.showAddOrgMsg = true;
        }
      })
        .catch(function(rejection) {

      });

    }

    $scope.init();

    $scope.createOrg = function(organization) {
      OrganizationService.register(organization.name)
        .then(function(sucess) {

        })
        .catch(function(error) {

        });
    }

    $scope.deleteOrg = function(organization, index) {
      //Confirmar accion
      OrganizationService.delete(organization.id)
        .then(function(sucess) {
          $scope.organizations.splice(index, 1 );
        })
        .catch(function(error) {

      });
    }

});
