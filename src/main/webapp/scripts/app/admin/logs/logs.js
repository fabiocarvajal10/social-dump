'use strict';

angular.module('socialdumpApp')
    .config(function($stateProvider) {
        $stateProvider
            .state('logs', {
                parent: 'admin',
                url: '/logs',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'Logs'
                },
                views: {
                    '': {
                        templateUrl: 'scripts/app/admin/logs/logs.html',
                        controller: 'LogsController'
                    }
                },
                resolve: {
                }
            });
    });
