(function() {
  'use strict';

  angular.module('socialdumpApp')
    .filter('trusted', ['$sce', function($sce) {
      return function(text) {
        return $sce.trustAsHtml(text);
      };
    }]);
}());
