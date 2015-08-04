/**
 * Created by fabio on 8/1/15.
 */
(function() {
  'use strict';
  angular.module('socialdumpApp.posts')
  .factory('EventPublic', [
    '$resource', 'DateUtils',
    function($resource, DateUtils) {
      return $resource('api/events-public/:id', {}, {
        'get': {
          method: 'GET',
          transformResponse: function(data) {
            data = angular.fromJson(data);
            data.startDate =
                DateUtils.convertDateTimeFromServer(data.startDate);
            data.endDate = DateUtils.convertDateTimeFromServer(data.endDate);
            data.activatedAt =
                DateUtils.convertDateTimeFromServer(data.activatedAt);
            return data;
          }
        }
      });
    }]);
}());
