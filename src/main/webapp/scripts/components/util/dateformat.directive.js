/**
 * Created by Franz on 30/07/2015.
 */
angular.module('socialdumpApp')
  .directive('dateFormatter', function($filter) {
    return {
      require: 'ngModel',
      link: function(scope, element, attrs, ngModelController) {
        ngModelController.$formatters.push(function(data) {
          if(data !== undefined){
            return $filter('date')(data, 'medium');
          }
        });
      }
    }
  });
