/**
* Validación de campos tipo fecha.
* Se valida que sea menor que un modelo dado.
* El modelo debe ser parte del Scope.
* @param {String} validateDateGreaterThanModel nombre del modelo al cual debe
*   ser mayor.
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
              ctrl.$validate();
              ctrl.$setDirty();
            };

            ctrl.$validators.dateGreaterThanModel = function() {
              var comparison = ObjectUtils.propertyOfNestedSequence(scope,
                attrs.validateDateGreaterThanModel);
              return ctrl.$$rawModelValue > comparison;
            };

            scope.$watch(attrs.ngModel, callback);
            scope.$watch(attrs.validateDateGreaterThanModel, callback);
          }
        };
  }]);
}());
