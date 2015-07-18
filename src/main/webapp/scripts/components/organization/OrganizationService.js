/**
 * Created by Franz on 17/07/2015.
 */
'use strict';

angular.module('socialdumpApp')
  .factory('OrganizationService', function ($http, $q, localStorageService) {
    return {
      register: function(organizationName) {
        var organization = {
          'name': organizationName,
          'ownerId': parseInt(localStorageService.get('userId'))
        }

        var q = $q.defer();
        $http({
          url: 'http://127.0.0.1:9090/api/organizations',
          method: 'POST',
          data: organization
        }).
        success(function(data){
          q.resolve(data);
        }).
        error(function(error){
          console.log(error);
          q.reject(error);
        });

        return q.promise;
      },

      getAll: function() {
        var q = $q.defer();
        $http({
          url: 'http://127.0.0.1:9090/api/organizations',
          method: 'GET'
        }).
        success(function(data){
          q.resolve(data);
        }).
        error(function(error){
           q.reject(error);
        });

        return q.promise;
      },

      delete: function(id) {
        alert(localStorageService.get('userId'));
        var q = $q.defer();
        $http({
          url: 'http://127.0.0.1:9090/api/organizations/' + id,
          method: 'DELETE',
          data: id
        }).
        success(function(data){
          q.resolve(data);
        }).
        catch(function(error){
          console.log(error);
          q.reject(error);
        });

        return q.promise;
      }
     };
   });
