'use strict';

angular.module('socialdumpApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('docs', {
                parent: 'home',
                url: 'docs',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'API'
                },
                templateUrl: 'scripts/app/admin/docs/docs.html'

            });
    });
