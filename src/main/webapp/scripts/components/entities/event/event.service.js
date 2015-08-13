(function() {
  'use strict';

  angular.module('socialdumpApp')
    .factory('Event', [
      '$resource', 'DateUtils', 'EventType', 'EventStatus', '$q',
      function($resource, DateUtils, EventType, EventStatus, $q) {
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
            url: 'api/events/cancel',
            interceptor: {
              responseError: function(error) {
                var errorMessage = '';

                if(error.data === 'Cant cancel a started event') {
                  errorMessage = 'El evento ya inició. No puede ser cancelado';
                } else if (error.data === 'The event already ended') {
                  errorMessage = 'El evento ya ha finalizado';
                } else {
                  errorMessage = 'Error inesperado al intentar cancelar el evento'
                }

                return $q.reject(errorMessage);
              }
            }
          }
        });
      }]);
}());
