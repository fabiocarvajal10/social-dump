/**
* Observa cambios en el estado de un modelo de una vista y dispara la validación
* de otro modelo.
* @param {String} validateControl nombre del modelo que se validará.
* @author Esteban
*/
(function() {
  'use strict';
  angular.module('socialdumpApp')
    .directive('validateControl', ['ObjectUtils',
      function(ObjectUtils) {
        return {
          require: 'ngModel',
          link: function(scope, ele, attrs) {
            var otherCtrl = ObjectUtils.propertyOfNestedSequence(
              scope, attrs.validateControl);
            var callback = function() {
              otherCtrl = otherCtrl || ObjectUtils.propertyOfNestedSequence(
                scope, attrs.validateControl);
              if (!!otherCtrl) {
                otherCtrl.$validate();
                otherCtrl.$setDirty();
              }
            };
            scope.$watch(attrs.ngModel, callback);
          }
        };
  }]);
}());
