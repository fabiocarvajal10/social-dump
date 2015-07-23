'use strict';

angular.module('socialdumpApp').controller('EventDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Event', 'Organization', 'EventStatus', 'EventType', 'SearchCriteria', 'TemporalAccess',
        function($scope, $stateParams, $modalInstance, entity, Event, Organization, EventStatus, EventType, SearchCriteria, TemporalAccess) {

        $scope.event = entity;
        $scope.organizations = Organization.query();
        $scope.eventstatuss = EventStatus.query();
        $scope.eventtypes = EventType.query();
        $scope.searchcriterias = SearchCriteria.query();
        $scope.temporalaccesss = TemporalAccess.query();
        $scope.load = function(id) {
            Event.get({id : id}, function(result) {
                $scope.event = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('socialdumpApp:eventUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.event.id != null) {
                Event.update($scope.event, onSaveFinished);
            } else {
                Event.save($scope.event, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
