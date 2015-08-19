'use strict';

angular.module('socialdumpApp')
    .config(function($stateProvider) {
        $stateProvider
            .state('admin', {
                abstract: true,
                parent: 'home',
                url: 'super',
                template: '<ui-view></ui-view>'
            });
    });
