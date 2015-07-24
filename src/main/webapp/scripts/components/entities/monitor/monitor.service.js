/**
 * Created by Franz on 23/07/2015.
 */
/**
 * Created by Franz on 17/07/2015.
 */
'use strict';

angular.module('socialdumpApp')
  .factory('MonitorService', function($http, $q) {
     return {
       getAll: function(organizationId) {
         var q = $q.defer();
         $http({
           url: 'api/monitor-contacts',
           method: 'GET',
           params: {
             'organizationId': 2
           }
         }).
         success(function(data) {
           q.resolve(data);
         }).
         error(function(error) {
           q.reject(error);
         });

         return q.promise;
       }
     };
   });

