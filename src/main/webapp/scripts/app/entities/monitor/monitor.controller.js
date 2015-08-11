/**
 * Created by Franz on 23/07/2015.
 */
angular.module('socialdumpApp.monitors')
  .controller('MonitorCtrl',
    function($scope, MonitorService, $modal) {
      $scope.totalItems = 0;
      $scope.currentPage = 1;
      $scope.monitorContacts = [];

      $scope.init = function() {
        MonitorService.getAll($scope.currentPage, 8)
          .then(function(data) {
            $scope.monitorContacts = data;
            $scope.totalItems = data.total;
          })
          .catch(function() {

          });
      };

      $scope.open = function(monitorContact, index, action) {

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

        }, function() {

        });
      };

      $scope.init();

      $scope.pageChanged = function() {
        MonitorService.getAll($scope.currentPage, 8)
          .then(function(data) {
            $scope.monitorContacts.splice(0, $scope.monitorContacts.length);
            $scope.monitorContacts = data;
            $scope.totalItems = data.total;
          })
          .catch(function() {

          });
      };

      function getModalUrl(action) {
        var baseUrl = 'scripts/app/entities/monitor/partials/';
        var extension = '.html';
        return baseUrl + action + extension;
      }

    });

