/**
 * Created by Franz on 17/07/2015.
 */
'use strict';

angular.module('socialdumpApp.organizations', [])
  .config(function($stateProvider) {
    $stateProvider
      .state('dashboard', {
        parent: 'home',
        url: 'dashboard/organizations',
        data: {
          roles: ['ROLE_USER'],
          pageTitle: 'Dashboard Organizaciones'
        },
        templateUrl: 'scripts/app/entities/organization/dashboard/dashboard.html',
        controller: 'OrganizationDashboardCtrl',
        resolve: {
          store: function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name: 'socialdumpApp.organizations',
              files: ['scripts/app/entities/organization/dashboard/organization-dashboard.controller.js',
                      'scripts/app/entities/temporal-access/temporal-access-detail.controller.js',
                      'scripts/components/entities/organization/organization.service.js'
              ]
            });
          }
        }
      })
      .state('organizations', {
        parent: 'home',
        url: 'organizations',
        data: {
          roles: ['ROLE_USER'],
          pageTitle: 'Organizaciones'
        },
        templateUrl: 'scripts/app/entities/organization/organizations.html',
        controller: 'OrganizationCtrl',
        resolve: {
          store: function($ocLazyLoad) {
            return $ocLazyLoad.load({
              name: 'socialdumpApp.organizations',
              files: ['scripts/app/entities/organization/organization.controller.js',
                      'scripts/app/entities/organization/organization-detail.controller.js',
                      'scripts/components/entities/organization/organization.service.js'
              ]
            });
          }
        }
      });
  });
