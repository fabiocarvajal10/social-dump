(function() {
  'use strict';

  /**
  * Servicio que obtiene resúmenes de evento por red social.
  */
  angular.module('socialdumpApp')
    .factory('EventSnSummary', ['$resource', function($resource) {
        return $resource('api/events/:id/sn-summary', {}, {
          'query': { method: 'GET', isArray: true}
        });
      }]);
}());
