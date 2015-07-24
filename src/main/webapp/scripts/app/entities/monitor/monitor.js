/**
 * Created by Franz on 23/07/2015.
 */
'use strict';

angular.module('socialdumpApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('monitors', {
        parent: 'home',
        url: 'monitors',
        data: {
          roles: [],
          pageTitle: 'Contactos de Monitoreo'
        },
        templateUrl: 'scripts/app/entities/monitor/monitors.html',
        controller: 'MonitorCtrl'
      });
  });
