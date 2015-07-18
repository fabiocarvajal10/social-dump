(function() {
  'use strict';

  angular.module('socialdumpApp')
    .factory('EventType', function($resource, DateUtils) {
      return $resource('api/event-types/:id', {}, {
        'query': { method: 'GET', isArray: true},
        'get': {
          method: 'GET',
          transformResponse: function(data) {
            data = angular.fromJson(data);
            return data;
          }
        },
        'update': { method: 'PUT' }
      });
    });
}());
