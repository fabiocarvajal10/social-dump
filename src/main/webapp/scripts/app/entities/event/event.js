(function() {
  'use strict';

  angular.module('socialdumpApp')
    .config(function($stateProvider) {
      $stateProvider
        .state('event', {
          abstract: true,
          parent: 'home',
          url: 'events',
          template: '<ui-view></ui-view>',
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
          url: 'event/{id}/detail',
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
            entity: ['$stateParams', 'Event', '$state',
              function($stateParams, Event, $state) {
                var success = function(result) {
                  return result;
                };
                var failure = function(error) {
                  if (error.status === 404) {
                    $state.go('404');
                  }
                  if (error.status === 403) {
                    $state.go('accessdenied');
                  }
                };

                return Event.getWithSummary({id: $stateParams.id},
                  success, failure);
              }
            ]
          }
        })
        .state('event.detail.summary', {
          parent: 'event.detail',
          url: '/summary',
          data: {
            roles: ['ROLE_USER'],
            pageTitle: 'Evento'
          },
          views: {
            'detail-tab-content': {
              templateUrl:
                'scripts/app/entities/event/event-detail-summary.html'
            }
          }
        })
        .state('event.detail.list', {
          parent: 'event.detail',
          url: '/list',
          data: {
            roles: ['ROLE_USER'],
            pageTitle: 'Evento'
          },
          views: {
            'detail-tab-content': {
              templateUrl: 'scripts/app/entities/event/event-detail-list.html'
            }
          }
        })
        .state('event.new', {
          parent: 'event',
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
                $state.go('event.list');
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
              $state.go('event.list');
            }, function() {
              $state.go('event.list');
            });
          }]
        });
    });
}());
