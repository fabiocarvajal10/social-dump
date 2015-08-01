(function() {
  'use strict';

  angular.module('socialdumpApp')
    .config(function($stateProvider) {
      $stateProvider
        .state('event', {
          abstract: true,
          parent: 'home',
          url: 'events',
          template: '<ui-view/>',
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
        .state('event.list', {
          parent: 'event',
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
        .state('event.detail', {
          parent: 'home',
          url: 'events/{id}',
          data: {
            roles: ['ROLE_USER'],
            pageTitle: 'Evento'
          },
          views: {
            '': {
              templateUrl: 'scripts/app/entities/event/event-detail.html',
              controller: 'EventDetailController'
            }
          },
          resolve: {
            entity: ['$stateParams', 'Event', function($stateParams, Event) {
              return Event.get({id: $stateParams.id});
            }]
          }
        })
        .state('event.new', {
          parent: 'event.list',
          url: '/new',
          data: {
            roles: ['ROLE_USER']
          },
          onEnter: [
            '$stateParams', '$state', '$modal',
            function($stateParams, $state, $modal) {
              $modal.open({
                templateUrl: 'scripts/app/entities/event/event-dialog.html',
                controller: 'EventDialogController',
                size: 'lg',
                resolve: {
                  entity: function() {
                    return {startDate: null, endDate: null, description: null,
                            activatedAt: null, postDelay: null, id: null};
                  }
                }
              }).result.then(function(result) {
                $state.go('event.list', null, { reload: true });
              }, function() {
                $state.go('event.list');
              });
          }]
        })
        .state('event.edit', {
          parent: 'event',
          url: '/{id}/edit',
          data: {
            roles: ['ROLE_USER']
          },
          onEnter: ['$stateParams', '$state', '$modal',
                    function($stateParams, $state, $modal) {
            $modal.open({
              templateUrl: 'scripts/app/entities/event/event-dialog.html',
              controller: 'EventDialogController',
              size: 'lg',
              resolve: {
                entity: ['Event', function(Event) {
                  return Event.get({id: $stateParams.id});
                }]
              }
            }).result.then(function(result) {
              $state.go('event.list', null, { reload: true });
            }, function() {
              $state.go('event.list');
            });
          }]
        });
    });
}());
