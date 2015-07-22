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
        }
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
      .state('events.detail', {
        // parent: 'entity',
        url: '/events/:id',
        data: {
          roles: ['ROLE_USER'],
          pageTitle: 'Event'
        },
        views: {
          '': {
            templateUrl: 'scripts/app/entities/event/event-detail.html',
            controller: 'EventDetailController'
          }
        },
        resolve: {
        }
      });
  });
