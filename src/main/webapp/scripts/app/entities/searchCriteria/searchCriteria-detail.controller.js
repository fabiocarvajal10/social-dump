'use strict';

angular.module('socialdumpApp')
    .controller('SearchCriteriaDetailController', function ($scope, $rootScope, $stateParams, entity, SearchCriteria, Event, SocialNetwork, GenericStatus, SocialNetworkPost) {
        $scope.searchCriteria = entity;
        $scope.load = function (id) {
            SearchCriteria.get({id: id}, function(result) {
                $scope.searchCriteria = result;
            });
        };
        $rootScope.$on('socialdumpApp:searchCriteriaUpdate', function(event, result) {
            $scope.searchCriteria = result;
        });
    });
