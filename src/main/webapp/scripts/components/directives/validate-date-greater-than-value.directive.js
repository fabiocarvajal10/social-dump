/**
* Validación de campos tipo fecha.
* Se valida que sea menor que un valor dado.
* El valor de la directiva debe ser una cadena de texto parseable a fecha
* a través de los mecanismos nativos de JavaScript.
* @author Esteban
*/
(function() {
  'use strict';
  angular.module('socialdumpApp')
    .directive('validateDateGreaterThan', function() {
      return {
        require: 'ngModel',
        link: function(scope, ele, attrs, ctrl) {
          scope.$watch(attrs.ngModel, function() {
            if (new Date(ctrl.$modelValue) >
                new Date(attrs.validateDateGreaterThan)) {
              ctrl.$setValidity('dateGreaterThan', true);
            } else {
              ctrl.$setValidity('dateGreaterThan', false);
            }
          });
        }
      };
  });
}());
