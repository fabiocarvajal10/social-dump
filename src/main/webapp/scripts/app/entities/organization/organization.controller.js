/**
 * Created by Franz on 17/07/2015.
 */
angular.module('socialdumpApp')
  .controller('OrganizationCtrl',
      function($scope, OrganizationService, $timeout) {
    $scope.organizations = [];
    $scope.incomingEventsByOrg = [];
    $scope.finalizedEventsByOrg = [];
    $scope.postsCount = [];
    $scope.modOrg = null;
    $scope.orgError = '';
    $scope.uptOrg = {};
    $scope.isUptOrg = false;
    $scope.showOrgError = false;

    $scope.init = function() {
      OrganizationService.getAll()
        .then(function(data) {
          $scope.organizations = data;
          if ($scope.organizations.length > 0) {
            $scope.changeOrgEvents($scope.organizations[0]);
          }
      })
        .catch (function(rejection) {

      });
    };

    $scope.init();

    $scope.createOrg = function(organization) {
      OrganizationService.register(organization.name)
        .then(function(newOrg) {
          organization.name = '';
          $scope.organizations.push(newOrg);
        })
        .catch (function(error) {
          showError(error);
        });
    };

    $scope.showUptOrg = function(organization, index) {
      $scope.isUptOrg = true;
      $scope.uptOrg.id = organization.id;
      $scope.uptOrg.name = organization.name;
      $scope.uptOrg.index = index;
    };

    $scope.updateOrg = function() {
      OrganizationService.update($scope.uptOrg)
        .then(function(updatedOrg) {
          $scope.organizations[$scope.uptOrg.index].name = updatedOrg.name;
          $scope.isUptOrg = false;
        })
        .catch (function(error) {
          showError(error);
        });
    };

    $scope.cancelUpdate = function() {
      $scope.isUptOrg = false;
    };

    $scope.deleteOrg = function(organization, index) {
      OrganizationService.delete(organization.id)
        .then(function(sucess) {
          $scope.organizations.splice(index, 1);
        })
        .catch (function(error) {
          showError(error);
      });
    };

    $scope.changeOrgEvents = function(organization) {
      OrganizationService.getIncomingEvents(organization.id)
        .then(function(data) {
          OrganizationService.setCurrentOrgId(organization.id);
          $scope.incomingEventsByOrg = data;
        })
        .catch (function(error) {

        });

      OrganizationService.getFinalizedEvents(organization.id)
        .then(function(data) {
          OrganizationService.setCurrentOrgId(organization.id);
          $scope.finalizedEventsByOrg = data;
        })
        .catch (function(error) {

        });

      OrganizationService.getOrgPostCount(organization.id)
        .then(function(data) {
         $scope.postsCount.splice(0, $scope.postsCount.length);
          processPostCount(data);
        })
        .catch (function(error) {

      });
    };

    $scope.changeEvent = function(event){
      OrganizationService.setCurrentEventId(event.id);
    };

    function processPostCount(postCount) {
      var total = 0;
      for(var key in postCount) {
        if (postCount.hasOwnProperty(key)) {
          var socialNetwork = {
            name: key,
            cant: postCount[key]
          }
          $scope.postsCount.push(socialNetwork);
          total += postCount[key];
        }
      }
      $scope.postsCount.push({
        name: 'Total',
        cant: total
      })

    };

    function showError(error) {
      $scope.orgError = error;
      $scope.showOrgError = true;
      $timeout(function() {
        $scope.showOrgError = false;
      }, 5000);
    };
});
