'use strict';

angular.module('socialdumpApp')
    .controller('SocialNetworkDetailController', function ($scope, $rootScope, $stateParams, entity, SocialNetwork) {
        $scope.socialNetwork = entity;
        $scope.load = function (id) {
            SocialNetwork.get({id: id}, function(result) {
                $scope.socialNetwork = result;
            });
        };
        $rootScope.$on('socialdumpApp:socialNetworkUpdate', function(event, result) {
            $scope.socialNetwork = result;
        });
    });
