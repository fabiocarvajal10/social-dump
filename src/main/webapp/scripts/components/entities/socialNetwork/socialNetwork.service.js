(function() {
  'use strict';

  angular.module('socialdumpApp')
    .factory('SocialNetwork', [
      '$resource', function($resource) {
        return $resource('api/social-networks/:id', {}, {
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
    }]);
}());
