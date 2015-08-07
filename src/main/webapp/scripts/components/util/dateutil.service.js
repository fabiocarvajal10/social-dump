(function() {
  'use strict';

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
      this.defaultDateTimeFormat = function() {
        return defaultDateTimeFormat;
      };

      this.halfHoursOfTheDay24Format = function() {
        var hours = [];
        for (var i = 0; i < 23; i++) {
          hours.push(i + ':00');
          hours.push(i + ':30');
        }
        return hours;
      };

      /**
      * Añade media hora a una fecha
      */
      this.toNextHalfHour = function(date) {
        // Media hora más
        var newDate = new Date(date.getTime() + 0.5 * 60 * 60 * 1000);
        newDate.setMinutes((newDate.getMinutes() < 30) ? 0 : 30);
        newDate.setSeconds(0);
        newDate.setMilliseconds(0);
        return newDate;
      };

      /**
      * Devuelve una fecha en texto, formato de 24 horas
      */
      this.toHourMinutesString = function(date) {
        return '' + date.getHours() + ':' + ('0' + date.getMinutes()).slice(-2);
      };

      /**
      * Devuelve los minutos de una cadena de texto
      */
      this.minutesOfString = function(timeString) {
        return timeString.slice(0, timeString.indexOf(':'));
      };

      /**
      * Devuelve las horas de una cadena de texto
      */
      this.hoursOfString = function(timeString) {
        return timeString.slice(timeString.indexOf(':') + 1);
      };
    });
}());
