'use strict';

angular.module('socialdumpApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('eventType', {
                parent: 'entity',
                url: '/eventType',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'EventTypes'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/eventType/eventTypes.html',
                        controller: 'EventTypeController'
                    }
                },
                resolve: {
                }
            })
            .state('eventTypeDetail', {
                parent: 'entity',
                url: '/eventType/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'EventType'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/eventType/eventType-detail.html',
                        controller: 'EventTypeDetailController'
                    }
                },
                resolve: {
                }
            });
    });
