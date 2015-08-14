(function() {
  'use strict';

  angular.module('socialdumpApp')
    .config(function($stateProvider) {
      $stateProvider
        .state('socialNetwork', {
          parent: 'home',
          url: '/socialNetworks',
          data: {
            roles: ['ROLE_USER', 'ROLE_ADMIN'],
            pageTitle: 'SocialNetworks'
          },
          views: {
            'content@': {
              templateUrl:
                'scripts/app/entities/socialNetwork/socialNetworks.html',
              controller: 'SocialNetworkController'
            }
          },
          resolve: {
          }
        })
        .state('socialNetwork.detail', {
          parent: 'home',
          url: '/socialNetwork/{id}',
          data: {
            roles: ['ROLE_USER', 'ROLE_ADMIN'],
            pageTitle: 'SocialNetwork'
          },
          views: {
            'content@': {
              templateUrl:
                'scripts/app/entities/socialNetwork/socialNetwork-detail.html',
              controller: 'SocialNetworkDetailController'
            }
          },
          resolve: {
            entity: ['$stateParams', 'SocialNetwork',
                     function($stateParams, SocialNetwork) {
              return SocialNetwork.get({id: $stateParams.id});
            }]
          }
        })
        .state('socialNetwork.new', {
          parent: 'socialNetwork',
          url: '/new',
          data: {
            roles: ['ROLE_USER', 'ROLE_ADMIN']
          },
          onEnter: ['$stateParams', '$state', '$modal',
                    function($stateParams, $state, $modal) {
            $modal.open({
              templateUrl:
                'scripts/app/entities/socialNetwork/socialNetwork-dialog.html',
              controller: 'SocialNetworkDialogController',
              size: 'lg',
              resolve: {
                entity: function() {
                  return {name: null, url: null, id: null};
                }
              }
            }).result.then(function(result) {
              $state.go('socialNetwork', null, { reload: true });
            }, function() {
              $state.go('socialNetwork');
            });
          }]
        })
        .state('socialNetwork.edit', {
          parent: 'socialNetwork',
          url: '/{id}/edit',
          data: {
            roles: ['ROLE_USER', 'ROLE_ADMIN']
          },
          onEnter: ['$stateParams', '$state', '$modal',
                    function($stateParams, $state, $modal) {
            $modal.open({
              templateUrl:
                'scripts/app/entities/socialNetwork/socialNetwork-dialog.html',
              controller: 'SocialNetworkDialogController',
              size: 'lg',
              resolve: {
                entity: ['SocialNetwork', function(SocialNetwork) {
                  return SocialNetwork.get({id: $stateParams.id});
                }]
              }
            }).result.then(function(result) {
              $state.go('socialNetwork', null, { reload: true });
            }, function() {
              $state.go('^');
            });
          }]
        });
    });
}());
