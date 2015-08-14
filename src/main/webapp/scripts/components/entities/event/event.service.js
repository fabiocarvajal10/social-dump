(function() {
  'use strict';

  angular.module('socialdumpApp')
    .factory('Event', [
      '$resource', 'DateUtils', 'EventType', 'EventStatus', 'EventSnSummary',
      'EventStatusUI',
      function($resource, DateUtils, EventType, EventStatus, EventSnSummary,
        EventStatusUI) {
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
              data.startDate =
                DateUtils.convertLocaleDateToServer(data.startDate);
              data.endDate = DateUtils.convertLocaleDateToServer(data.endDate);
              data.activatedAt =
                DateUtils.convertLocaleDateToServer(data.activatedAt);
              return angular.toJson(data);
            }
          }
        });
      }]);
}());
