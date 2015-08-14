(function() {
  'use strict';

  /**
  * Módulo de utilitarios para manejar fechas.
  */
  angular.module('socialdumpApp')
    .service('DateUtils', function() {
      var defaultDateTimeFormat = 'd/M/yy h:mm a';

      this.convertLocaleDateToServer = function(date) {
        if (date) {
          var utcDate = new Date();
          utcDate.setUTCDate(date.getDate());
          utcDate.setUTCMonth(date.getMonth());
          utcDate.setUTCFullYear(date.getFullYear());
          return utcDate;
        } else {
          return null;
        }
      };

      this.convertLocaleDateFromServer = function(date) {
        if (date) {
          var dateString = date.split('-');
          return new Date(dateString[0], dateString[1] - 1, dateString[2]);
        }
        return null;
      };

      this.convertDateTimeFromServer = function(date) {
        if (date) {
          return new Date(date);
        } else {
          return null;
        }
      };

      /**
      * Devuelve el formato de fecha por defecto.
      */
      this.defaultDateTimeFormat = function() {
        return defaultDateTimeFormat;
      };

      /**
      * Devuelve las medias horas de todo un día.
      */
      this.halfHoursOfTheDay24Format = function() {
        var hours = [];
        for (var i = 0; i < 24; i++) {
          hours.push(i + ':00');
          hours.push(i + ':30');
        }
        return hours;
      };

      /**
      * Añade media hora a una fecha
      * @param {Date} date Objeto Javascript fecha
      */
      this.toNextHalfHour = function(date) {
        if (!date) {
          return this.toNextHalfHour(new Date());
        }
        // Media hora más
        var newDate = new Date(date.getTime() + 0.5 * 60 * 60 * 1000);
        newDate.setMinutes((newDate.getMinutes() < 30) ? 0 : 30);
        newDate.setSeconds(0);
        newDate.setMilliseconds(0);
        return newDate;
      };

      /**
      * Devuelve una fecha en texto, formato de 24 horas
      * @param {Date} date Objeto Javascript fecha
      */
      this.toHourMinutesString = function(date) {
        if (!date) {
          return '0:00';
        }
        return '' + date.getHours() + ':' + ('0' + date.getMinutes()).slice(-2);
      };

      /**
      * Devuelve las horas de una cadena de texto.
      * @param {String} timeString tiempo en formato texto. Solo tiempo, no
      *   incluye días.
      */
      this.hoursOfString = function(timeString) {
        if (!timeString) {
          return 0;
        }
        return timeString.slice(0, timeString.indexOf(':'));
      };

      /**
      * Devuelve los minutos de una cadena de texto.
      * @param {String} timeString tiempo en formato texto. Solo tiempo, no
      *   incluye días.
      */
      this.minutesOfString = function(timeString) {
        if (!timeString) {
          return 0;
        }
        return timeString.slice(timeString.indexOf(':') + 1);
      };

      /**
      * Determina si una fecha termina en una media hora exacta.
      * @param {String} timeString tiempo en formato texto. Solo tiempo, no
      *   incluye días.
      */
      this.isExactHalfHour = function(timeString) {
        if (!timeString) {
          return false;
        }
        var mins = parseInt(this.minutesOfString(timeString));
        if (mins === 0 || mins === 30) {
          return true;
        }
        return false;
      };

      /**
      * Obtiene la siguiente media hora más cercana.
      * @param {Date} date Objeto fecha.
      */
      this.nextHalfHour = function(date) {
        if (!date) {
          return this.nextHalfHour(new Date());
        }
        var timeStr = this.toHourMinutesString(date);
        if (this.isExactHalfHour(timeStr)) {
          return date;
        }
        return this.toNextHalfHour(date);
      };


      /**
      * Obtiene la fecha de hoy a las 12:00 media noche.
      */
      this.todayMidnight = function() {
        var today = new Date();
        today.setHours(0);
        today.setMinutes(0);
        today.setSeconds(0);
        today.setMilliseconds(0);
        return today;
      };

      this.isDateLowerThanNow = function(date) {
        var now = new Date().toISOString();
        return date < now;
      };
    });
}());
