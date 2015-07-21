'use strict';

angular.module('socialdumpApp')
  .config(function($stateProvider) {
    $stateProvider
      .state('account', {
        abstract: true,
        parent: 'site'
      });
  });
