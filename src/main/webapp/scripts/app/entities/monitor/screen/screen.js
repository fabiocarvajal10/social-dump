/**
 * Created by fabio on 7/17/15.
 */
(function() {
  'use strict';
  angular.module('socialdumpApp.monitor-screen', []);
  angular.module('socialdumpApp')
    .config(function($stateProvider) {
      $stateProvider
        .state('monitor-screen', {
          parent: 'site',
          url: '/event-monitor/:id',
          data: {
            roles: ['ROLE_USER'],
            pageTitle: 'Post Tracker'
          },
          views: {
            'content@': {
              templateUrl: 'scripts/app/entities/monitor/screen/screen.html',
              controller: 'MonitorScreenController'
            }
          },
          resolve: {
            store: function($ocLazyLoad) {
              return $ocLazyLoad.load([
                {
                  name: 'socialdumpApp.posts',
                  files: ['scripts/components/entities/post/post.service.js',
                    'scripts/components/entities/post/post-tracker.service.js',
                    'scripts/components/entities/post/cards.service.js',
                    'scripts/components/entities/post/event-public.service.js',
                    'scripts/components/util/playlist.service.js',
                    'scripts/app/entities/monitor/screen/unsync-detail.controller.js'
                  ]
                },
                {
                  name: 'socialdumpApp.monitor-screen',
                  files: ['scripts/app/entities/monitor/screen/screen.controller.js']
                }

              ]);
            }
          },
          onEnter: ['PostTracker', '$stateParams',
            function(PostTracker, $stateParams) {
            //Loading existing data when entry
              PostTracker.connect($stateParams.id);
              PostTracker.subscribePublic();
              PostTracker.subscribeMonitor();
              //MonitorPostTracker.connect();
              //MonitorPostTracker.subscribe();
            }],
          onExit: function(PostTracker) {
            PostTracker.unsubscribe();
          }
        });
    });
}());
