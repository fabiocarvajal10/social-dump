(function() {
  'use strict';

  angular.module('socialdumpApp')
    .config(function($stateProvider) {
      $stateProvider
        .state('eventStatus', {
          parent: 'home',
          url: 'event-statuses',
          data: {
            roles: ['ROLE_USER'],
            pageTitle: 'Estados de eventos'
          },
          views: {
            'content@': {
              templateUrl:
                'scripts/app/entities/event-status/event-statuses.html',
              controller: 'EventStatusController'
            }
          },
          resolve: {
          }
        })
        .state('eventStatusDetail', {
          parent: 'home',
          url: 'event-statuses/:id',
          data: {
            roles: ['ROLE_USER'],
            pageTitle: 'Estadosde eventos'
          },
          views: {
            'content@': {
              templateUrl:
                'scripts/app/entities/event-status/eventStatus-detail.html',
              controller: 'EventStatusDetailController'
            }
          },
          resolve: {
          }
        });
    });
}());
