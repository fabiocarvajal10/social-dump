'use strict';

angular.module('socialdumpApp').controller('SocialNetworkDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'SocialNetwork',
        function($scope, $stateParams, $modalInstance, entity, SocialNetwork) {

        $scope.socialNetwork = entity;
        $scope.load = function(id) {
            SocialNetwork.get({id : id}, function(result) {
                $scope.socialNetwork = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('socialdumpApp:socialNetworkUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.socialNetwork.id != null) {
                SocialNetwork.update($scope.socialNetwork, onSaveFinished);
            } else {
                SocialNetwork.save($scope.socialNetwork, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
