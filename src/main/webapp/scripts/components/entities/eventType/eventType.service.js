(function() {
  'use strict';

  angular.module('socialdumpApp')
    .factory('EventType', ['$resource', function($resource) {
      return $resource('api/event-types/:id', {}, {
        'query': { method: 'GET', isArray: true},
        'get': {
          method: 'GET',
          transformResponse: function(data) {
            data = angular.fromJson(data);
            return data;
          },
          cache: true
        },
        'update': { method: 'PUT' }
      });
    }]);
}());
