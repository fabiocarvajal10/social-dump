/**
 * Created by Franz on 30/07/2015.
 */
angular.module('socialdumpApp')
  .directive('dtPicker', function(){
    return {
      restrict: 'A',
      scope: false,
      controller: function($scope, $element){
        $scope.onSetTime = function(){
          angular.element(document.querySelector('.open')).removeClass('open');
        }
      }
    }
  });
