/**
 * Created by Franz on 17/07/2015.
 */
'use strict';

angular.module('socialdumpApp')
  .factory('AccountService', function ($http, $q) {
     return {
       getUserId: function() {
         var q = $q.defer();
         $http({
           url: 'http://127.0.0.1:9090/api/account',
           method: 'GET'
         }).
         success(function(data){
           q.resolve(data.id.toString());
         }).
         catch(function(error){
           q.reject(undefined);
         });

         return q.promise;
       }
     };
  });

