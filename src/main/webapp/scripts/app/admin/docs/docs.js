(function() {
  'use strict';

  angular.module('socialdumpApp')
    .config(function($stateProvider) {
    $stateProvider
      .state('docs', {
      parent: 'admin',
      url: '/docs',
      data: {
        roles: ['ROLE_ADMIN'],
        pageTitle: 'API'
      },
      views: {
        '': {
        templateUrl: 'scripts/app/admin/docs/docs.html'
        }
      }
      });
    });
}());
