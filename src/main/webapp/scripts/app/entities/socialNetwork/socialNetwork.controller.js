'use strict';

angular.module('socialdumpApp')
    .controller('SocialNetworkController', function ($scope, SocialNetwork, ParseLinks) {
        $scope.socialNetworks = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            SocialNetwork.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.socialNetworks.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.socialNetworks = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            SocialNetwork.get({id: id}, function(result) {
                $scope.socialNetwork = result;
                $('#deleteSocialNetworkConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            SocialNetwork.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteSocialNetworkConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.socialNetwork = {name: null, url: null, id: null};
        };
    });
