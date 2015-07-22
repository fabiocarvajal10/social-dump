'use strict';

angular.module('socialdumpApp')
    .controller('EventStatusDetailController', function ($scope, $stateParams, EventStatus, Event) {
        $scope.eventStatus = {};
        $scope.load = function (id) {
            EventStatus.get({id: id}, function(result) {
              $scope.eventStatus = result;
            });
        };
        $scope.load($stateParams.id);
    });
