'use strict';

angular.module('socialdumpApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('events', {
        abstract: true,
        parent: 'home',
        template: '<ui-view/>',
        url: 'events',
        data: {
          roles: ['ROLE_USER'],
          pageTitle: 'Eventos'
        }/*,
        views: {
          'content@': {
            templateUrl: 'scripts/app/entities/event/events.html',
            controller: 'EventController'
          }
        },
        resolve: {
        }*/
      })
      .state('events.list', {
        // parent: 'events',
        url: '',
        data: {
          roles: ['ROLE_USER'],
          pageTitle: 'Eventos'
        },
        views: {
          '': {
            templateUrl: 'scripts/app/entities/event/events.html',
            controller: 'EventController'
          }
        },
        resolve: {
        }
      })
      .state('event', {
        parent: 'entity',
        url: '/event',
        data: {
          roles: ['ROLE_USER'],
          pageTitle: 'Events'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/entities/event/events.html',
            controller: 'EventController'
          }
        },
        resolve: {
        }
      })
      .state('eventDetail', {
        parent: 'entity',
        url: '/event/:id',
        data: {
          roles: ['ROLE_USER'],
          pageTitle: 'Event'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/entities/event/event-detail.html',
            controller: 'EventDetailController'
          }
        },
        resolve: {
        }
      });
  });
