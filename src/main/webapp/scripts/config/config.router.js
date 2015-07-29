(function() {
  'use strict';
  /**
   * Config for the router
   */
  const viewsPrfx = 'app/views/';
  angular.module('app')
    .run([
      '$rootScope', '$state', '$stateParams',
      function($rootScope, $state, $stateParams) {
        $rootScope.$state = $state;
        $rootScope.$stateParams = $stateParams;
      }])
    .config([
      '$stateProvider', '$urlRouterProvider', 'JQ_CONFIG', 'MODULE_CONFIG',
      function($stateProvider, $urlRouterProvider, JQ_CONFIG, MODULE_CONFIG) {
        var layout = viewsPrfx + 'layouts/app.html';
        $urlRouterProvider
          .otherwise('/app/');
        $stateProvider
            .state('app', {
                // abstract: true,
                url: '/app',
                templateUrl: layout
            })
            .state('app.dashboard', {
                url: '/',
                templateUrl: viewsPrfx + 'dashboard/app_dashboard_v1.html'/*,
                resolve: load(['app/controllersJustForTemplate/chart.js'])*/
            })
            .state('app.calendario', {
                url: '/calendario',
                templateUrl: viewsPrfx + 'calendario/calendario.html',
                resolve: load(['moment', 'fullcalendar', 'ui.calendar',
                               'app/app/calendar/calendar.js'])
            })
            //Contests
            //using inheritance
            .state('app.contests', {
              abstract: true,
              template: '<ui-view/>',
              url: '/contests',
              controller: 'ContestCtrl'
            })
            .state('app.contests.list', {
              url: '',
              templateUrl: viewsPrfx + 'contests/list.html'
            })
            .state('app.contests.create', {
              url: '/create',
              templateUrl: viewsPrfx + 'concursos/create.html'
            })
            .state('app.contests.edit', {
              url: '/edit',
              templateUrl: viewsPrfx + 'concursos/edit.html'
            })
            .state('app.contests.show', {
              url: '/show',
              templateUrl: viewsPrfx + 'concursos/show.html'
            })
            //Events
            .state('app.events', {
              abstract: true,
              template: '<ui-view/>',
              url: '/events' ,
              controller: 'EventCtrl'
            })
            .state('app.events.list', {
              url: '' ,
              templateUrl: viewsPrfx + 'events/list.html'
            })
            .state('app.events.create', {
              url: '/create' ,
              templateUrl: viewsPrfx + 'events/create.html'
            })
            .state('app.events.edit', {
              url: '/edit' ,
              templateUrl: viewsPrfx + 'events/edit.html'
            })
            .state('app.events.show', {
              url: '/show' ,
              templateUrl: viewsPrfx + 'events/show.html'
            })
            // Monitor / Event
            .state('app.monitors', {
              abstract: true,
              template: '<ui-view/>',
              url: '/monitors' ,
              controller: 'MonitorCtrl'
            })
            .state('app.monitors.show', {
              url: '' ,
              templateUrl: viewsPrfx + 'monitors/show.html'
            })
            // Playlists
            .state('app.playlists', {
              abstract: true,
              template: '<ui-view/>',
              url: '/playlists',
              controller: 'PlaylistCtrl'
            })
            .state('app.playlists.show', {
              url: '/:id',
              templateUrl: viewsPrfx + 'playlists/show.html'
            })
            //Activities
            .state('app.activities', {
              abstract: true,
              template: '<ui-view/>',
              url: '/activities' ,
              controller: 'ActivityCtrl'
            })
            .state('app.activities.list', {
              url: '' ,
              templateUrl: viewsPrfx + 'activities/list.html'
            })
            .state('app.activities.create', {
              url: '/create' ,
              templateUrl: viewsPrfx + 'activities/create.html'
            })
            .state('app.activities.edit', {
              url: '/edit' ,
              templateUrl: viewsPrfx + 'activities/edit.html'
            })
            .state('app.activities.show', {
              url: '/show' ,
              templateUrl: viewsPrfx + 'activities/show.html'
            })
            //Organizations
            .state('app.organizations', {
              abstract: true,
              template: '<ui-view/>',
              url: '/organizations' ,
              controller: 'OrganizationCtrl'
            })
            .state('app.organizations.list', {
              url: '' ,
              templateUrl: viewsPrfx + 'organizations/list.html'
            })
            .state('app.organizations.create', {
              url: '/create' ,
              templateUrl: viewsPrfx + 'organizations/create.html'
            })
            .state('app.organizations.edit', {
              url: '/edit' ,
              templateUrl: viewsPrfx + 'organizations/edit.html'
            })
            .state('app.organizations.show', {
              url: '/show' ,
              templateUrl: viewsPrfx + 'organizations/show.html'
            })
            //Monitor contacts
            .state('app.contacts', {
              abstract: true,
              template: '<ui-view/>',
              url: '/contacts' ,
              controller: 'ContactCtrl'
            })
            .state('app.contacts.list', {
              url: '' ,
              templateUrl: viewsPrfx + 'contacts/list.html'
            })
            .state('app.contacts.create', {
              url: '/create' ,
              templateUrl: viewsPrfx + 'contacts/create.html'
            })
            .state('app.contacts.edit', {
              url: '/edit' ,
              templateUrl: viewsPrfx + 'contacts/edit.html'
            })
            .state('app.contacts.show', {
              url: '/show' ,
              templateUrl: viewsPrfx + 'contacts/show.html'
            })

            .state('app.planes', {
              url: '/planes',
              templateUrl: viewsPrfx + 'planes/planes.html'
            })
            .state('app.pagar', {
              url: '/pagar',
              templateUrl: viewsPrfx + 'pagos/pagar.html'
            })
            .state('app.actividades', {
              url: '/actividades',
              templateUrl: viewsPrfx + 'actividades/actividades.html',
              controller: 'ActividadCtrl'
            });


            function load(srcs, callback) {
              return {
                  deps: ['$ocLazyLoad', '$q',
                    function($ocLazyLoad, $q) {
                      var deferred = $q.defer();
                      var promise = false;
                      srcs = angular.isArray(srcs) ? srcs : srcs.split(/\s+/);
                      if (!promise) {
                        promise = deferred.promise;
                      }
                      angular.forEach(srcs, function(src) {
                        promise = promise.then(function() {
                          if (JQ_CONFIG[src]) {
                            return $ocLazyLoad.load(JQ_CONFIG[src]);
                          }
                          angular.forEach(MODULE_CONFIG, function(module) {
                            if (module.name == src) {
                              name = module.name;
                            } else {
                              name = src;
                            }
                          });
                          return $ocLazyLoad.load(name);
                        });
                      });
                      deferred.resolve();
                      return callback ?
                        promise.then(function() { return callback(); }) :
                        promise;
                  }]
              };
            }
        }
      ]
    );
}());
