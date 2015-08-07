/**
* Validación de campos tipo fecha
* @author Esteban
*/
(function() {
  'use strict';
  angular.module('socialdumpApp')
    .directive('ensureDateGreaterThan', function() {
      return {
        require: 'ngModel',
        link: function(scope, ele, attrs, c) {
          scope.$watch(attrs.ngModel, function() {
            if (new Date(c.$modelValue) >
                new Date(attrs.ensureDateGreaterThan)) {
              c.$setValidity('dateGreaterThan', true);
            } else {
              c.$setValidity('dateGreaterThan', false);
            }
          });
        }
      };
  });
}());
