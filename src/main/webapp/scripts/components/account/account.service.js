(function() {
  'use strict';
  angular.module('socialdumpApp')
    .factory(
      'AccountService', [
        '$http', '$q', 'localStorageService', '$rootScope',
        function($http, $q, localStorageService, $rootScope) {
         return {
           getUserId: function() {
             var q = $q.defer();
             $http({
               url: 'api/account',
               method: 'GET'
             }).
             success(function(data) {
               var loggedUser = {
                 login: data.login,
                 firstName: data.firstName,
                 lastName: data.lastName,
                 email: data.email
               }
               localStorageService.set('loggedUser', loggedUser);
               q.resolve(data.id.toString());
             }).
             catch (function(error) {
               q.reject(undefined);
             });

             return q.promise;
           },

           delete: function(password) {
             var cred = "id=" + parseInt(localStorageService.get('userId')) +
                        "&password=" + password;
             var q = $q.defer();
             $http({
               headers: {
                 "Content-Type": "application/x-www-form-urlencoded",
                 "Accept": "*/*"
               },
               url: 'api/delete',
               method: 'POST',
               data: cred
             }).
             success(function(data) {
               q.resolve(data);
             }).
             catch (function(error) {
               if(error.status === 409) {
                 error = 'La contraseña no es valida';
               } else {
                 error = 'Error inesperado al intentar eliminar la cuenta';
               }
               q.reject(error);
           });

           return q.promise;

           }
         };
      }]);
  }());
