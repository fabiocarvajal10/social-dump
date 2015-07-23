/**
 * Created by Franz on 17/07/2015.
 */
(function() {
  'use strict';
  angular.module('socialdumpApp')
    .factory(
      'AccountService', [
        '$http', '$q',
        function($http, $q) {
         return {
           getUserId: function() {
             var q = $q.defer();
             $http({
               url: 'api/account',
               method: 'GET'
             }).
             success(function(data) {
               q.resolve(data.id.toString());
             }).
             catch (function(error) {
               q.reject(undefined);
             });

             return q.promise;
           }
         };
      }]);
  }());
