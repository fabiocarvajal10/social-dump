/**
 * Created by Franz on 17/07/2015.
 */
'use strict';

angular.module('socialdumpApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('dashboard', {
        parent: 'home',
        url: 'dashboard/organizations',
        data: {
          roles: ['ROLE_USER'],
          pageTitle: 'Dashboard Organizaciones'
        },
        templateUrl: 'scripts/app/entities/organization/dashboard.html',
        controller: 'OrganizationCtrl'
      });
  });
