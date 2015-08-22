/**
 * Created by Franz on 14/08/2015.
 */
(function() {
  'use strict';
  angular.module('socialdumpApp.monitor-screen')
    .controller('UnsyncDetailController',
    ['$scope', '$modalInstance', 'event', 'searchCriteria', 'Event',
     function($scope, $modalInstance, event, searchCriteria, Event) {

       $scope.confirmUnsync = function() {
         var index = event.allSearchCriterias.indexOf(searchCriteria);
         Event.stopSync({
           'eventId': event.id,
           'searchCriteria': searchCriteria
         }, function() {
           event.allSearchCriterias.splice(index, 1);
           $modalInstance.close();
         });
       };

       $scope.cancel = function() {
         $modalInstance.dismiss('cancel');
       };
     }]
  );
})();
