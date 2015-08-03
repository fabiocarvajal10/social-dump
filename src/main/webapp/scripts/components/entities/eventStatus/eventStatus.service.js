(function() {
  'use strict';

  angular.module('socialdumpApp')
    .factory('EventStatus', function($resource) {
      return $resource('api/event-statuses/:id', {}, {
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
