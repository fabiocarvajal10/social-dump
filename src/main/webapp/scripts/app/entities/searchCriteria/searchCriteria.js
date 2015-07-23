(function() {
  'use strict';

  angular.module('socialdumpApp')
    .config(function($stateProvider) {
      $stateProvider
        .state('searchCriteria', {
          parent: 'entity',
          url: '/searchCriterias',
          data: {
            roles: ['ROLE_USER'],
            pageTitle: 'SearchCriterias'
          },
          views: {
            '': {
              templateUrl:
                'scripts/app/entities/searchCriteria/searchCriterias.html',
              controller: 'SearchCriteriaController'
            }
          },
          resolve: {
          }
        })
        .state('searchCriteria.detail', {
          parent: 'entity',
          url: '/searchCriteria/{id}',
          data: {
            roles: ['ROLE_USER'],
            pageTitle: 'SearchCriteria'
          },
          views: {
            '': {
              templateUrl:
                'scripts/app/entities/searchCriteria/searchCriteria-detail.html',
              controller: 'SearchCriteriaDetailController'
            }
          },
          resolve: {
            entity: ['$stateParams', 'SearchCriteria',
                     function($stateParams, SearchCriteria) {
              return SearchCriteria.get({id: $stateParams.id});
            }]
          }
        })
        .state('searchCriteria.new', {
          parent: 'searchCriteria',
          url: '/new',
          data: {
            roles: ['ROLE_USER']
          },
          onEnter: ['$stateParams', '$state', '$modal',
                    function($stateParams, $state, $modal) {
            $modal.open({
              templateUrl:
                'scripts/app/entities/searchCriteria/searchCriteria-dialog.html',
              controller: 'SearchCriteriaDialogController',
              size: 'lg',
              resolve: {
                entity: function() {
                  return {searchCriteria: null, id: null};
                }
              }
            }).result.then(function(result) {
              $state.go('searchCriteria', null, { reload: true });
            }, function() {
              $state.go('searchCriteria');
            });
          }]
        })
        .state('searchCriteria.edit', {
          parent: 'searchCriteria',
          url: '/{id}/edit',
          data: {
            roles: ['ROLE_USER']
          },
          onEnter: [
            '$stateParams', '$state', '$modal',
            function($stateParams, $state, $modal) {
              $modal.open({
                templateUrl:
                  'scripts/app/entities/searchCriteria/searchCriteria-dialog.html',
                controller: 'SearchCriteriaDialogController',
                size: 'lg',
                resolve: {
                  entity: ['SearchCriteria', function(SearchCriteria) {
                    return SearchCriteria.get({id: $stateParams.id});
                  }]
                }
              }).result.then(function(result) {
                $state.go('searchCriteria', null, { reload: true });
              }, function() {
                $state.go('^');
              });
          }]
        });
    });
}());
