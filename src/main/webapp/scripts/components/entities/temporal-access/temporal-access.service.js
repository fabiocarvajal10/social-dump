/**
 * Created by Franz on 27/07/2015.
 */
'use strict';

angular.module('socialdumpApp.temporalAccess')
  .factory('TemporalAccessService', function($http, $q, localStorageService) {
    return {
      register: function(monitorContact) {
        monitorContact.organizationId = 2;//parseInt(localStorageService.get('organizationId'));
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
          if(error.data === 'e-mail address already in use'){
            error = 'Ya cuenta con un contacto de monitoreo con el mismo correo electrónico';
          }else{
            error = 'Error inesperado al intentar crear el contacto de monitoreo';
          }
          q.reject(error);
        });

       return q.promise;
      },

      getAll: function (eventId) {
        var q = $q.defer();
        $http({
          url: 'api/temporal-accesses',
          method: 'GET',
          params: {
          //ToDo Definir como se va a elegir la organización actual
            'eventId': 81//parseInt(localStorageService.get('eventId'))
          }
        }).
        success(function (data) {
          q.resolve(data);
        }).
        error(function (error) {
          q.reject(error);
        });

        return q.promise;
      }
     }
    });
