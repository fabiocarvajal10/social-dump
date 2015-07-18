'use strict';

angular.module('socialdumpApp')
    .controller('EventDetailController', function ($scope, $stateParams, Event, Organization, EventStatus, EventType, SearchCriteria, TemporalAccess) {
        $scope.event = {};
        $scope.load = function (id) {
            Event.get({id: id}, function(result) {
              $scope.event = result;
            });
        };
        $scope.load($stateParams.id);
    });
