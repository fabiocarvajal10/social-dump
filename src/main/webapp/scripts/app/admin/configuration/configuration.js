'use strict';

angular.module('socialdumpApp')
  .config(function($stateProvider) {
    $stateProvider
        .state('configuration', {
            parent: 'admin',
            url: '/configuration',
            data: {
                roles: ['ROLE_ADMIN'],
                pageTitle: 'Configuración'
            },
            views: {
                '': {
                    templateUrl: 'scripts/app/admin/configuration/configuration.html',
                    controller: 'ConfigurationController'
                }
            },
            resolve: {
            }
        });
  });
