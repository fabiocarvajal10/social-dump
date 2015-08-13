(function() {
  'use strict';

  angular.module('socialdumpApp')
    .factory('Event', [
      '$resource', 'DateUtils', 'EventType', 'EventStatus',
      function($resource, DateUtils, EventType, EventStatus) {
        return $resource('api/events/:id', {}, {
          'query': { method: 'GET', isArray: true},
          'get': {
            method: 'GET',
            transformResponse: function(data) {
              data = angular.fromJson(data);
              data.type = EventType.get({id: data.typeId});
              data.status = EventStatus.get({id: data.statusId});
              data.startDate =
                DateUtils.convertDateTimeFromServer(data.startDate);
              data.endDate = DateUtils.convertDateTimeFromServer(data.endDate);
              data.activatedAt =
                DateUtils.convertDateTimeFromServer(data.activatedAt);
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
          },
          'cancel': {
            method: 'POST',
            params: {id: '@id'},
            url: 'api/events/cancel'
          }
        });
      }]);
}());
