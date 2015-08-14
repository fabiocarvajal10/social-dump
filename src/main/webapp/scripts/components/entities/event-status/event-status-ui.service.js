(function() {
  'use strict';

  /**
  * Utilitario para elementos de UI correspondientes a un estado de un evento.
  * @author Esteban Trejos
  */
  angular.module('socialdumpApp')
    .service('EventStatusUI', function() {

      var bootstrapClassSuffixes = {
        'Activo': 'success',
        'Cancelado': 'error',
        'Inactivo': 'warning',
        'Pendiente': 'info'
      };

      /**
      * Obtiene un sufijo de clase de CSS de bootstrap correspondiente a un
      * estado de un evento en formato String.
      * Maps a status in String format to a bootstrap class suffix.
      * @param {String} statusStr status in String format.
      * this.bootstrapClassSuffixes('Activo)')
      * @return {String} Bootstrap class suffix.
      */
      this.bootstrapClassSuffix = function(statusStr) {
        return bootstrapClassSuffixes[statusStr];
      };

      /**
      * Devuelve un mensaje al obtener un status.
      * @param {String} statusStr cadena de texto del status.
      * @param {Boolean} addDescription si añade descripción
      * @return {String} HTML con un mensaje estilizado que indica el estado.
      *
      */
      this.messageForStatus = function(statusStr, addDescription) {
        if (!statusStr) {
          return '';
        }
        var cssBsClassSfx = bootstrapClassSuffixes[statusStr];
        if (!cssBsClassSfx) {
          return '';
        }
        return '<div class="alert alert-' + cssBsClassSfx + '">' +
               '<strong>' + ((addDescription && !!cssBsClassSfx) ?
               'El evento está ' : '') +
               statusStr.toLowerCase() +
               '.</strong></div>';
      };
    });
}());
