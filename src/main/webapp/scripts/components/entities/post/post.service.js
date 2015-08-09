/**
 * Created by fabio on 7/25/15.
 */
'use strict';
angular.module('socialdumpApp.posts')
.factory('PostService', [
    '$resource', 'DateUtils',
    function($resource, DateUtils) {
      return $resource('api/social-network-posts/event/:id', {}, {
        'query': { method: 'GET', isArray: true},
        'get': {
          method: 'GET',
          transformResponse: function(data) {
            data = angular.fromJson(data);
            data.createdAt = DateUtils
              .convertLocaleDateFromServer(data.createdAt);
            return data;
          }
        }
      });
    }
  ]);
