/**
 * Created by Franz on 17/08/2015.
 */
(function() {
  'use strict';

  angular.module('socialdumpApp')
    .factory('OwnerService', ['$rootScope', '$q', 'Event', '$state',
      'localStorageService', 'TemporalAccessService',
      function($rootScope, $q, Event, $state, localStorageService,
          TemporalAccessService) {
        return {
          'validate': function(eventId){
            if (localStorageService.get('tempAccessId') === undefined
                || localStorageService.get('tempAccessId') === null) {
              Event.validateOwnership({id:eventId})
                .$promise.then(function(data) {

                }, function() {
                  $state.go('dashboard');
                });
            } else {
              TemporalAccessService.validate(eventId)
              .then(function(data){

              })
              .catch(function(error){
                localStorageService.clearAll();
                $state.go('access');
              });
            }
          }
        };
      }]);
}());
