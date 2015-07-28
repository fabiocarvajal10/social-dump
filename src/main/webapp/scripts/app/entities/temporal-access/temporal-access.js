/**
 * Created by Franz on 27/07/2015.
 */
'use strict';

angular.module('socialdumpApp.temporalAccess', [])
  .config(function($stateProvider) {
    $stateProvider
      .state('accesses', {
        parent: 'home',
        url: 'accesses',
        data: {
          roles: ['ROLE_USER'],
          pageTitle: 'Accesos Temporales'
        },
        templateUrl: 'scripts/app/entities/temporal-access/temporal-access.html',
        controller: 'TemporalAccessCtrl',
        resolve: {
          store: function ($ocLazyLoad) {
            return $ocLazyLoad.load({
              name: 'socialdumpApp.temporalAccess',
              files: ['scripts/app/entities/temporal-access/temporal-access.controller.js',
                      'scripts/app/entities/temporal-access/temporal-access-detail.controller.js',
                      'scripts/components/entities/temporal-access/temporal-access.service.js'
              ]
            });
          }
        }
      });
  });
