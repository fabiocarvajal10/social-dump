/**
 * Created by Franz on 17/08/2015.
 */
(function() {
  'use strict';

  angular.module('socialdumpApp')
    .factory('OwnerService', ['$rootScope', '$q', 'Event', '$state',
      function($rootScope, $q, Event, $state) {
        return {
          'validate': function(eventId){
            if ($rootScope.tempAccessId === undefined || $rootScope.tempAccessId === null) {
              Event.validateOwnership({id:eventId})
                .$promise.then(function(data) {

                }, function() {
                  $state.go('dashboard');
                });
            }
          }
        };
      }]);
}());
