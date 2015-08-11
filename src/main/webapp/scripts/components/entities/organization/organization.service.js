/**
 * Created by Franz on 17/07/2015.
 */
'use strict';

angular.module('socialdumpApp')
  .factory('OrganizationService', function($http, $q, localStorageService) {
    var currOrgId = 1;
    return {
      register: function(organizationName) {
        var organization = {
          'name': organizationName,
          'ownerId': parseInt(localStorageService.get('userId'))
        };
        var currentTime = new Date();
        var q = $q.defer();
        $http({
          url: 'api/organizations',
          method: 'POST',
          data: organization
        }).
        success(function(data, status, headers) {
          organization.id = parseInt(headers('Location').match(/[0-9]+/g));
          organization.createdAt = currentTime;
          currOrgId = organization.id;
          q.resolve(organization);
        }).
        catch (function(error) {
          var err = error.data.exception;
          if (err ===
              'org.springframework.dao.DataIntegrityViolationException') {
            err = 'Ya cuenta con una organización del mismo nombre';
          }else {
            err = 'Error inesperado al intentar crear la organización';
          }
          q.reject(err);
        });

        return q.promise;
      },

      getAll: function(page, limit) {
        var q = $q.defer();
        $http({
          url: 'api/organizations',
          method: 'GET',
          params: {
            'page': page,
            'per_page': limit
          }
        }).
        success(function(data, status, headers) {
          data.total = parseInt(headers('X-Total-Count'));
          q.resolve(data);
        }).
        error(function(error) {
          q.reject(error);
        });

        return q.promise;
      },

      getNewest: function() {
        var q = $q.defer();
        $http({
          url: 'api/organizations/newest',
          method: 'GET',
        }).
        success(function(data) {
          q.resolve(data);
        }).
        error(function(error) {
          q.reject(error);
        });

        return q.promise;
      },

      getIncomingEvents: function(organizationId) {
        var q = $q.defer();
        $http({
          url: 'api/events/incoming',
          method: 'GET',
          params: {
            'organizationId': organizationId
          }
        }).
        success(function(data) {
          q.resolve(data);
        }).
        catch(function(error) {
          q.reject(error);
        });

        return q.promise;
      },

      getFinalizedEvents: function(organizationId) {
        var q = $q.defer();
        $http({
          url: 'api/events/finalized',
          method: 'GET',
          params: {
            'organizationId': organizationId
          }
        }).
        success(function(data) {
          q.resolve(data);
        }).
        catch(function(error) {
          q.reject(error);
        });

        return q.promise;
      },

      update: function(organization) {
        var q = $q.defer();
        $http({
          url: 'api/organizations',
          method: 'PUT',
          params: {
            'id': organization.id,
            'name': organization.name
          }
        }).
        success(function(data) {
          q.resolve(organization);
        }).
        catch (function(error) {
          var err = error.data.exception;
          if (err === 'org.springframework.dao.DataIntegrityViolationException') {
            err = 'Ya cuenta con una organización del mismo nombre';
          }else {
            err = 'Error inesperado al intentar modificar la organización';
          }
          q.reject(err);
        });

        return q.promise;
      },

      delete: function(id) {
        var q = $q.defer();
        $http({
          url: 'api/organizations/' + id,
          method: 'DELETE',
          data: id
        }).
        success(function(data) {
          q.resolve(data);
        }).
        catch (function(error) {
          var err = error.data.exception;
          if (err ===
              'org.springframework.dao.DataIntegrityViolationException') {
            err = 'No se puede eliminar la organización.' +
                  ' La organización cuenta con eventos';
          }else {
            err = 'Error inesperado al intentar eliminar la organización';
          }
          q.reject(err);
        });

        return q.promise;
      },

      getOrgPostCount: function(organizationId) {
        var q = $q.defer();
        $http({
          url: 'api//social-network-posts/count',
          method: 'GET',
          params: { 'organizationId': organizationId}
        }).
        success(function(data) {
          q.resolve(data);
        }).
        catch (function(error) {
          q.reject(error);
        });

        return q.promise;
      },

      setCurrentOrgId: function(organizationId){
        localStorageService.set('orgId', organizationId);
      },

      getCurrentEventId: function() {
        return parseInt(localStorageService.get('eventId'));
      },

      setCurrentEventId: function(eventId){
        localStorageService.set('eventId', eventId);
      },

      getCurrentOrgId: function() {
        return parseInt(localStorageService.get('orgId'));
      }

     };
   });
