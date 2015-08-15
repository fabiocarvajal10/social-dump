/**
 * Created by Franz on 23/07/2015.
 */
angular.module('socialdumpApp.monitors')
  .controller('MonitorCtrl', ['$rootScope', '$scope',
    'MonitorService', '$modal',
    function($rootScope, $scope, MonitorService, $modal) {
      $scope.totalItems = 0;
      $scope.currentPage = 1;
      $scope.monitorContacts = [];

      $rootScope
        .$on('currentOrganizationChange', function(event, args) {
          getMonitors();
        });

      $scope.open = function(monitorContact, index, action) {
        checkMonitorsCant();
        var modalInstance = $modal.open({
          animation: true,
          templateUrl: getModalUrl(action),
          controller: 'MonitorDetailCtrl',
          resolve: {
            'monitorContacts': function() {
              return $scope.monitorContacts;
            },
            'monitorContact': function() {
              return monitorContact;
            },
            'index': function() {
              return index;
            }
          }
        });

        modalInstance.result.then(function() {
          checkMonitorsCant();
        }, function() {

        });
      };

      $scope.pageChanged = function() {
        getMonitors();
      };


      function isOrgEmpty() {
        return $rootScope.currentOrg === null ||
            $rootScope.currentOrg === undefined;
      };

      function getMonitors() {
        if (!isOrgEmpty()) {
          MonitorService.getAll($scope.currentPage, 8)
            .then(function(data) {
              $scope.monitorContacts = data;
              $scope.totalItems = data.total;
            })
            .catch(function() {

            });
        }
      };

      function getModalUrl(action) {
        var baseUrl = 'scripts/app/entities/monitor/partials/';
        var extension = '.html';
        return baseUrl + action + extension;
      };

      function checkMonitorsCant() {
        if ($scope.monitorContacts.length === 0 && $scope.currentPage !== 1) {
          $scope.currentPage--;
          getMonitors();
        } else if ($scope.monitorContacts.length > 8) {
          $scope.currentPage++;
          getMonitors();
        }
      }

      getMonitors();
    }
  ]);

