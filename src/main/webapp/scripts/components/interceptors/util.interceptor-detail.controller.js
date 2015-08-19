/**
 * Created by Franz on 13/08/2015.
 */
(function() {
  'use strict';
  angular.module('socialdumpApp')
    .controller('ModalDetailInterceptorCtrl',
    ['$scope', '$modalInstance', 'message',
     function($scope, $modalInstance, message) {
       $scope.message = message;
       $scope.cancel = function() {
         $modalInstance.dismiss('cancel');
       };
     }]
  );
})();
