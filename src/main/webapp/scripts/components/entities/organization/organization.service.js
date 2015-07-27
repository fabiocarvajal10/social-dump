(function() {
  'use strict';

  angular.module('socialdumpApp')
    .factory('Organization', function($resource, DateUtils) {
      return $resource('api/organizations/:id', {}, {
        'query': { method: 'GET', isArray: true},
        'get': {
          method: 'GET',
          transformResponse: function(data) {
            data = angular.fromJson(data);
            data.createdAt =
              DateUtils.convertLocaleDateFromServer(data.createdAt);
            return data;
          }
        },
        'update': {
          method: 'PUT',
          transformRequest: function(data) {
            data.createdAt =
              DateUtils.convertLocaleDateToServer(data.createdAt);
            return angular.toJson(data);
          }
        },
        'save': {
          method: 'POST',
          transformRequest: function(data) {
            data.createdAt =
              DateUtils.convertLocaleDateToServer(data.createdAt);
            return angular.toJson(data);
          }
        }
      });
    });
}());
