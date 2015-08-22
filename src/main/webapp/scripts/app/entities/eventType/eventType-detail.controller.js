'use strict';

angular.module('socialdumpApp')
    .controller('EventTypeDetailController', function ($scope, $stateParams, EventType, Event) {
        $scope.eventType = {};
        $scope.load = function (id) {
            EventType.get({id: id}, function(result) {
              $scope.eventType = result;
            });
        };
        $scope.load($stateParams.id);
    });
