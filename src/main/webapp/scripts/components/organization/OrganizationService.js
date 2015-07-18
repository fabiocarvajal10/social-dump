/**
 * Created by Franz on 17/07/2015.
 */
(function() {
  'use strict';

  angular.module('socialdumpApp')
    .factory(
      'OrganizationService', [
        '$http', '$q', 'localStorageService', '$location',
        function($http, $q, localStorageService, $location) {
          var rootUrl = $location.protocol() + '://' + $location.host() + ':' +
                        $location.port();
          var orgsUrl = rootUrl + '/api/organizations';
          var currOrgId = 1;

<<<<<<< HEAD
=======
        var q = $q.defer();
        $http({
          url: 'http://127.0.0.1:9090/api/organizations',
          method: 'POST',
          data: organization
        }).
        success(function(data, status, headers){
          organization['id'] = parseInt(headers('Location').match(/[0-9]+/g));
          q.resolve(organization);
        }).
        catch(function(error){
          var err = error.data.exception;
          if(err === 'org.springframework.dao.DataIntegrityViolationException') {
            err = 'Ya cuenta con una organización del mismo nombre';
          }else {
            err = 'Error inesperado al intentar crear la organización';
          }
          q.reject(err);
        });
>>>>>>> development

          return {
            register: function(organizationName) {
              var organization = {
                'name': organizationName,
                'ownerId': parseInt(localStorageService.get('userId'))
              };

<<<<<<< HEAD
              var q = $q.defer();
              $http({
                url: 'http://127.0.0.1:9090/api/organizations',
                method: 'POST',
                data: organization
              }).
              success(function(data) {
                q.resolve(data);
              }).
              error(function(error) {
                console.log(error);
                q.reject(error);
              });
=======
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

      getAllEvents: function(organizationId) {
        var q = $q.defer();
        $http({
          url: 'http://127.0.0.1:9090/api/events',
          method: 'GET',
          params: {
            'organizationId': organizationId
          }
        }).
          success(function(data){
            q.resolve(data);
          }).
          error(function(error){
            q.reject(error);
          });

        return q.promise;
      },

      update: function(organization) {
        var q = $q.defer();
        $http({
          url: 'http://127.0.0.1:9090/api/organizations',
          method: 'PUT',
          params: {
            'id': organization.id,
            'name': organization.name
          }
        }).
        success(function(data){
          q.resolve(organization);
        }).
        catch(function(error) {
          var err = error.data.exception;
          if(err === 'org.springframework.dao.DataIntegrityViolationException') {
            err = 'Ya cuenta con una organización del mismo nombre';
          }else {
            err = 'Error inesperado al intentar modificar la organización';
          }
          q.reject(err);
        });
>>>>>>> development

              return q.promise;
            },

<<<<<<< HEAD
            getAll: function() {
              var q = $q.defer();
              $http({
                url: 'http://127.0.0.1:9090/api/organizations',
                method: 'GET'
              }).
              success(function(data) {
                q.resolve(data);
              }).
              error(function(error) {
                 q.reject(error);
              });
=======
      delete: function(id) {
        var q = $q.defer();
        $http({
          url: 'http://127.0.0.1:9090/api/organizations/' + id,
          method: 'DELETE',
          data: id
        }).
        success(function(data){
          q.resolve(data);
        }).
        catch(function(error) {
          var err = error.data.exception;
          if(err === 'org.springframework.dao.DataIntegrityViolationException') {
            err = 'No se puede eliminar la organización. La organización cuenta con eventos';
          }else {
            err = 'Error inesperado al intentar eliminar la organización';
          }
          q.reject(err);
        });
>>>>>>> development

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
              success(function(data) {
                q.resolve(data);
              }).
              catch (function(error) {
                console.log(error);
                q.reject(error);
              });

              return q.promise;
            },

             getCurrentOrgId: function() {
              return currOrgId;
             }
           };
         }]);
}());
