(function() {
  'use strict';

  angular.module('socialdumpApp')
    .factory('Event', [
      '$resource', 'DateUtils', 'EventType', 'EventStatus', 'EventSnSummary',
      'EventStatusUI', '$q',
      function($resource, DateUtils, EventType, EventStatus, EventSnSummary,
        EventStatusUI, $q) {
        var addDateTimeProperties = function(event) {
          event.startDate =
            DateUtils.convertDateTimeFromServer(event.startDate);
          event.endDate = DateUtils.convertDateTimeFromServer(event.endDate);
          event.activatedAt =
            DateUtils.convertDateTimeFromServer(event.activatedAt);
          event.startDate =
            DateUtils.nextHalfHour(event.startDate);
          event.endDate =
            DateUtils.nextHalfHour(event.endDate);
          event.startTime =
            DateUtils.toHourMinutesString(event.startDate);
          event.endTime =
            DateUtils.toHourMinutesString(event.endDate);
        };
        var loadSummaries = function(event) {
          event.summary = event.summary || {};
          event.summary.snSummariesTotal = [];
          event.summary.snSummaries =
            EventSnSummary.query({id: event.id}, function() {
              event.summary.postsAvailable = true;
              event.summary.grandTotal = 0;
              for (var i = event.summary.snSummaries.length - 1; i >= 0; i--) {
                event.summary.snSummariesTotal.push(
                  event.summary.snSummaries[i].total);
                event.summary.grandTotal += event.summary.snSummaries[i].total;
              }
              event.summary.snSummariesTotal.reverse();
          });
        };
        var addEventStatusMessage = function(event) {
          if (event.endDate < new Date()) {
            event.status.status = 'Finalizado';
          } else if (event.startDate > new Date()) {
            event.status.status = 'Pendiente';
          }
          if (event && event.status) {
            event.status.message =
              EventStatusUI.messageForStatus(event.status.status, true);
          }
        };
        return $resource('api/events/:id', {}, {
          'query': { method: 'GET', isArray: true},
          'get': {
            method: 'GET',
            transformResponse: function(data) {
              data = angular.fromJson(data);
              data.type = EventType.get({id: data.typeId});
              data.status = EventStatus.get({id: data.statusId});
              addDateTimeProperties(data);
              return data;
            }
          },
          'getWithSummary': {
            method: 'GET',
            transformResponse: function(data) {
              data = angular.fromJson(data);
              data.type = EventType.get({id: data.typeId});
              data.status = EventStatus.get({id: data.statusId},
                function() {addEventStatusMessage(data);});
              addDateTimeProperties(data);
              loadSummaries(data);
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
              /*data.startDate =
                DateUtils.convertLocaleDateToServer(data.startDate);
              data.endDate = DateUtils.convertLocaleDateToServer(data.endDate);*/
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

                if (error.data === 'Cant cancel a started event') {
                  errorMessage = 'El evento ya inició. No puede ser cancelado';
                } else if (error.data === 'The event already ended') {
                  errorMessage = 'El evento ya ha finalizado';
                } else {
                  errorMessage =
                    'Error inesperado al intentar cancelar el evento';
                }

                return $q.reject(errorMessage);
              }
            }
          },
          'stopSync': {
            method: 'POST',
            params: {
              eventId: '@eventId',
              searchCriteria: '@searchCriteria'
            },
            url: 'api/events/synchronization/kill/',
            interceptor: {
              responseError: function(error) {
                return $q.reject(error);
              }
            }
          },
          'validateOwnership': {
            method: 'POST',
            params: {
              id: '@id'
            },
            url: 'api/events/owner/validate/'
          }
        });
      }]);
}());
