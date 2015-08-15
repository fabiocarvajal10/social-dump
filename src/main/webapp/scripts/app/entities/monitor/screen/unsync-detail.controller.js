/**
 * Created by Franz on 14/08/2015.
 */
(function() {
  'use strict';
  angular.module('socialdumpApp.monitor-screen')
    .controller('UnsyncDetailController',
    ['$scope', '$modalInstance', 'event', 'searchCriteria',
     function($scope, $modalInstance, event, searchCriteria) {

       $scope.confirmUnsync = function() {
         var index = event.searchCriterias.indexOf(searchCriteria);
         $modalInstance.close();
       };

       $scope.cancel = function() {
         $modalInstance.dismiss('cancel');
       };
     }]
  );
})();
