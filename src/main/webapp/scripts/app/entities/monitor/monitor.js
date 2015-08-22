/**
 * Created by Franz on 23/07/2015.
 */
'use strict';

angular.module('socialdumpApp.monitors', [])
  .config(function($stateProvider) {
    $stateProvider
      .state('monitors', {
        parent: 'home',
        url: 'monitors',
        data: {
          roles: ['ROLE_USER'],
          pageTitle: 'Contactos de Monitoreo'
        },
        templateUrl: 'scripts/app/entities/monitor/monitors.html',
        controller: 'MonitorCtrl',
        resolve: {
          store: function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name: 'socialdumpApp.monitors',
              files: ['scripts/app/entities/monitor/monitor.controller.js',
                      'scripts/app/entities/monitor/monitor-detail.controller.js',
                      'scripts/components/entities/monitor/monitor.service.js'
              ]
            });
          }
        }
      });
  });
