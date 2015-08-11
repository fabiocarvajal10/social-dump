/**
 * Created by Franz on 23/07/2015.
 */
'use strict';

angular.module('socialdumpApp.monitors')
  .factory('MonitorService', function($http, $q, OrganizationService) {
     return {
       register: function(monitorContact) {
         monitorContact.organizationId = OrganizationService.getCurrentOrgId();
         var q = $q.defer();
         $http({
           url: 'api/monitor-contacts',
           method: 'POST',
           data: monitorContact
         }).
         success(function(data) {
           monitorContact.id = parseInt(data);
           q.resolve(monitorContact);
         }).
         catch (function(error) {
           if (error.data === 'e-mail address already in use') {
             error = 'Ya cuenta con un contacto de monitoreo ' +
                     'con el mismo correo electrónico';
           }else {
             error = 'Error inesperado al intentar crear ' +
                     'el contacto de monitoreo';
           }
           q.reject(error);
         });

         return q.promise;
       },

       getAll: function(page, limit) {
         var q = $q.defer();
         $http({
           url: 'api/monitor-contacts',
           method: 'GET',
           params: {
             'page': page,
             'per_page': limit,
             'organizationId': OrganizationService.getCurrentOrgId()
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

       update: function(monitorContact) {
         var q = $q.defer();
         $http({
           url: 'api/monitor-contacts/',
           method: 'PUT',
           data: monitorContact
         }).
         success(function(data) {
          q.resolve(monitorContact);
         }).
         catch (function(error) {
           if (error.data === 'e-mail address already in use') {
             error = 'Ya cuenta con un contacto de monitoreo ' +
                     'con el mismo correo electrónico';
           }else {
             error = 'Error inesperado al intentar modificar ' +
                     'el contacto de monitoreo';
           }
           q.reject(error);
         });

         return q.promise;
       },

       delete: function(id) {
         var q = $q.defer();
         $http({
           url: 'api/monitor-contacts/' + id,
           method: 'DELETE',
           data: id
         }).
         success(function(data) {
          q.resolve(data);
         }).
         catch (function(error) {
          var err = 'Error al eliminar el contacto de monitoreo';
          q.reject(err);
         });

         return q.promise;
       }
     };
   });

