(function() {
  'use strict';

  /**
  * Utilitario para manejo de objetos de JavaScript.
  */
  angular.module('socialdumpApp')
    .service('ObjectUtils', function() {
      /**
      * Obtiene un modelo del Scope.
      * El modelo puede ser una propiedad de otro objeto.
      * @param {object} objeto del que se partirá a obtener las propiedades
      * @param {string} nestedProperty cadena de texto que indica la
      *   secuencia de propiedades que se deben seguir para alcanzar el
      *   modelo
      */
      this.propertyOfNestedSequence = function(source, nestedProperty) {
        var model = source;
        var properties = nestedProperty.split('.');
        for (var i = 0; i < properties.length; i++) {
          model = model[properties[i]];
        }
        return model;
      };
    });
}());
