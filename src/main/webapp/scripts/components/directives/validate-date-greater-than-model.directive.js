/**
* Validación de campos tipo fecha.
* Se valida que sea menor que un modelo dado.
* El modelo debe ser parte del Scope.
* @author Esteban
*/
(function() {
  'use strict';
  angular.module('socialdumpApp')
    .directive('validateDateGreaterThanModel', [
      'ObjectUtils',
      function(ObjectUtils) {
        return {
          require: 'ngModel',
          link: function(scope, ele, attrs, ctrl) {
            var callback = function() {
              var sourceModel = ObjectUtils.propertyOfNestedSequence(scope,
                attrs.ngModel);
              var comparison = ObjectUtils.propertyOfNestedSequence(scope,
                attrs.validateDateGreaterThanModel);
              if (sourceModel > comparison) {
                ctrl.$setValidity('dateGreaterThanModel', true);
              } else {
                ctrl.$setValidity('dateGreaterThanModel', false);
                ctrl.$setDirty();
              }
            };

            scope.$watch(attrs.ngModel, callback);
            scope.$watch(attrs.validateDateGreaterThanModel, callback);
          }
        };
  }]);
}());
