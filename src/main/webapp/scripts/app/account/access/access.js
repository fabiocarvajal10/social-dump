/**
 * Created by Franz on 02/08/2015.
 */
(function() {
  'use strict';

  angular.module('socialdumpApp.access', [])
    .config(function($stateProvider) {
      $stateProvider
        .state('access', {
          parent: 'account',
          url: '/access',
          data: {
            roles: [],
            pageTitle: 'Acceso Temporal'
          },
          views: {
            'content@': {
              templateUrl: 'scripts/app/account/access/access.html',
              controller: 'AccessController'
            }
          },
          resolve: {
            store: function($ocLazyLoad){
              return $ocLazyLoad.load({
                name: 'socialdumpApp.access',
                files: ['scripts/app/account/access/access.controller.js']
              });
            }
          }
        });
      });
}());
