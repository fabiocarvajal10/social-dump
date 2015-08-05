/**
 * Created by Franz on 27/07/2015.
 */
'use strict';

angular.module('socialdumpApp.temporalAccess')
  .factory('TemporalAccessService', function($http, $q, OrganizationService) {
    return {
      register: function(temporalAccess) {
        temporalAccess.organizationId = OrganizationService.getCurrentOrgId();
        temporalAccess.eventId = 81;
        temporalAccess.monitorContactId =
          temporalAccess.monitorContactByMonitorContactId.id;
        if (temporalAccess.allEvent) {
          temporalAccess.startDate = null;
          temporalAccess.endDate = null;
        }
        var q = $q.defer();
        $http({
          url: 'api/temporal-accesses',
          method: 'POST',
          data: temporalAccess
        }).
        success(function(data) {
          temporalAccess.id = parseInt(data);
          q.resolve(temporalAccess);
        }).
        catch (function(error) {
          if (error.data === 'e-mail address already in use') {
            error = 'Ya cuenta con un contacto de monitoreo' +
                    'con el mismo correo electrónico';
          }else if (error.data === 'Monitor cant access before the event') {
            error = 'El acceso temporal no puede iniciar antes del evento';
          }else if (error.data === 'Monitor cant access after the event') {
            error = 'El acceso temporal no puede finalizar después del evento';
          }else {
            error = 'Error inesperado al intentar crear' +
                    'el contacto de monitoreo';
          }
          q.reject(error);
        });

       return q.promise;
      },

      getAll: function(eventId) {
        var q = $q.defer();
        $http({
          url: 'api/temporal-accesses',
          method: 'GET',
          params: {
          //ToDo Definir como se va a elegir la organización actual
            'eventId': 81//parseInt(localStorageService.get('eventId'))
          }
        }).
        success(function(data) {
          q.resolve(data);
        }).
        error(function(error) {
          q.reject(error);
        });

        return q.promise;
      },

      delete: function(id) {
        var q = $q.defer();
        $http({
          url: 'api/temporal-accesses/' + id,
          method: 'DELETE',
          data: id
        }).
        success(function(data) {
          q.resolve(data);
        }).
        catch (function(error) {
          var err = 'Error al eliminar el acceso temporal';
          q.reject(err);
        });

        return q.promise;
      }
     };
    });
