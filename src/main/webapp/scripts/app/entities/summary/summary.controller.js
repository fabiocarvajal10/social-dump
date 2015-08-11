/**
 * Created by fabio on 8/11/15.
 */
'use strict';
angular.module('socialdumpApp.summary')
  .controller('SummaryCtrl', ['$scope', '$timeout',
      'OrganizationService', 'localStorageService',
      function($scope, $timeout, OrganizationService, localStorageService) {

    $scope.finalizedEventsByOrg = [];
    $scope.postsCount = [];
    $scope.summaryError = '';
    $scope.showSummaryError = false;

    $scope.$watch(
      function() {
        return localStorageService.get('orgId');
      },
      function(newValue, oldValue) {
        console.log('Change Org');
        if (newValue !== oldValue && newValue !== undefined) {
          updateFinalizedEvents(parseInt(newValue));
        }
      }
    );

    var updateFinalizedEvents = function(currentOrgId) {
      OrganizationService.getFinalizedEvents(currentOrgId)
        .then(function(data) {
          $scope.orgEvents = data;
          $scope.finalizedEventsByOrg = data;
      })
        .catch(function(rejection) {

      });
    };

    $scope.init = function() {
      updateFinalizedEvents(OrganizationService.getCurrentOrgId());
    };

    $scope.init();

    function processPostCount(postCount) {
      var total = 0;
      for (var key in postCount) {
        if (postCount.hasOwnProperty(key)) {
          var socialNetwork = {
            name: key,
            cant: postCount[key]
          };
          $scope.postsCount.push(socialNetwork);
          total += postCount[key];
        }
      }
      $scope.postsCount.push({
        name: 'Total',
        cant: total
      });

    };

    /*
    function showError(error) {
      $scope.orgError = error;
      $scope.showOrgError = true;
      $timeout(function() {
        $scope.showOrgError = false;
      }, 5000);
    };
    */
}]);
