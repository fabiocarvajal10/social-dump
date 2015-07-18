'use strict';

angular.module('socialdumpApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('eventStatus', {
                parent: 'entity',
                url: '/event-statuses',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Estados de eventos'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/eventStatus/eventStatuss.html',
                        controller: 'EventStatusController'
                    }
                },
                resolve: {
                }
            })
            .state('eventStatusDetail', {
                parent: 'entity',
                url: '/event-statuses/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Estadosde eventos'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/eventStatus/eventStatus-detail.html',
                        controller: 'EventStatusDetailController'
                    }
                },
                resolve: {
                }
            });
    });
