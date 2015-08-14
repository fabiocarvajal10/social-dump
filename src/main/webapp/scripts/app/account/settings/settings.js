'use strict';

angular.module('socialdumpApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('settings', {
                parent: 'home',
                url: 'settings',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Settings'
                },
                templateUrl: 'scripts/app/account/settings/settings.html',
                controller: 'SettingsController',
                resolve: {

                }
            });
    });
