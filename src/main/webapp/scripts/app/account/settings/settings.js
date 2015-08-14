'use strict';

angular.module('socialdumpApp.settings', [])
  .config(function($stateProvider) {
    $stateProvider
      .state('settings', {
        parent: 'home',
        url: 'settings',
        data: {
            roles: ['ROLE_USER'],
            pageTitle: 'Modificar Cuenta'
        },
        templateUrl: 'scripts/app/account/settings/settings.html',
        controller: 'SettingsController',
        resolve: {
          store: function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name: 'socialdumpApp.settings',
              files: ['scripts/app/account/settings/settings.controller.js',
                      'scripts/app/account/settings/settings-detail.controller.js'
              ]
            });
          }
        }
      });
  });
