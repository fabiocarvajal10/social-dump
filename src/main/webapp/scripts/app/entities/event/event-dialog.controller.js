(function() {
  'use strict';

  angular.module('socialdumpApp').controller('EventDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Event',
     // 'Organization',
     'EventType', 'SearchCriteria',
     'OrganizationService',
      function($scope, $stateParams, $modalInstance, entity, Event,
               EventType, SearchCriteria, OrganizationService) {
        $scope.event = entity;
        // $scope.eventstatuses = EventStatus.query();
        $scope.eventTypes = EventType.query();
        $scope.load = function(id) {
          Event.get({id: id}, function(result) {
            $scope.event = result;
          });
        };

        var onSaveFinished = function(result) {
          $scope.$emit('socialdumpApp:eventUpdate', result);
          $modalInstance.close(result);
        };

        $scope.save = function() {
          if ($scope.event.id !== null) {
            Event.update($scope.event, onSaveFinished);
          } else {
            $scope.event.organizationId = OrganizationService.getCurrentOrgId();
            Event.save($scope.event, onSaveFinished);
          }
        };

        $scope.clear = function() {
          $modalInstance.dismiss('cancel');
        };
    }]);
}());
