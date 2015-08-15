'use strict';

angular.module('socialdumpApp').controller('SearchCriteriaDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'SearchCriteria', 'Event', 'SocialNetwork', 'GenericStatus', 'SocialNetworkPost',
        function($scope, $stateParams, $modalInstance, entity, SearchCriteria, Event, SocialNetwork, GenericStatus, SocialNetworkPost) {

        $scope.searchCriteria = entity;
        $scope.events = Event.query();
        $scope.socialnetworks = SocialNetwork.query();
        $scope.genericstatuss = GenericStatus.query();
        $scope.socialnetworkposts = SocialNetworkPost.query();
        $scope.load = function(id) {
            SearchCriteria.get({id : id}, function(result) {
                $scope.searchCriteria = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('socialdumpApp:searchCriteriaUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.searchCriteria.id != null) {
                SearchCriteria.update($scope.searchCriteria, onSaveFinished);
            } else {
                SearchCriteria.save($scope.searchCriteria, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
