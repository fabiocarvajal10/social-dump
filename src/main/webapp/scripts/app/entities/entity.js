(function() {
  'use strict';

  angular.module('socialdumpApp')
    .config(['$stateProvider', function($stateProvider) {
      $stateProvider
        .state('entity', {
          abstract: true,
          parent: 'home'
        });
    }]);
}());
