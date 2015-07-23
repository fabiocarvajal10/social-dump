(function() {
  'use strict';

  angular.module('socialdumpApp')
    .factory('Event', function($resource, DateUtils) {
      return $resource('api/events/:id', {}, {
        'query': { method: 'GET', isArray: true},
        'get': {
          method: 'GET',
          transformResponse: function(data) {
            data = angular.fromJson(data);
            data.startDate =
              DateUtils.convertLocaleDateFromServer(data.startDate);
            data.endDate = DateUtils.convertLocaleDateFromServer(data.endDate);
            data.activatedAt =
              DateUtils.convertLocaleDateFromServer(data.activatedAt);
            return data;
          }
        },
        'update': {
          method: 'PUT',
          transformRequest: function(data) {
            data.startDate =
              DateUtils.convertLocaleDateToServer(data.startDate);
            data.endDate = DateUtils.convertLocaleDateToServer(data.endDate);
            data.activatedAt =
              DateUtils.convertLocaleDateToServer(data.activatedAt);
            return angular.toJson(data);
          }
        },
        'save': {
          method: 'POST',
          transformRequest: function(data) {
            data.startDate =
              DateUtils.convertLocaleDateToServer(data.startDate);
            data.endDate = DateUtils.convertLocaleDateToServer(data.endDate);
            data.activatedAt =
              DateUtils.convertLocaleDateToServer(data.activatedAt);
            return angular.toJson(data);
          }
        }
      });
    });
}());
