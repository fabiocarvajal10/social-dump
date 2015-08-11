/**
 * Created by fabio on 8/11/15.
 */
(function() {
  'use strict';
  angular.module('socialdumpApp.summary', []);
  angular.module('socialdumpApp')
    .config(function($stateProvider) {
      $stateProvider
        .state('summaries', {
          parent: 'home',
          url: 'summaries/organization',
          data: {
            roles: ['ROLE_USER'],
            pageTitle: 'Resúmen de eventos'
          },
          views: {
            '': {
              templateUrl: 'scripts/app/entities/summary/summary.html',
              controller: 'SummaryCtrl',
            }
          },
          resolve: {
            store: function($ocLazyLoad) {
              return $ocLazyLoad.load(
                {
                  name: 'socialdumpApp.summary',
                  files: ['scripts/app/entities/summary/summary.controller.js']
                }
              );
            }
          }
        });
    });
}());
