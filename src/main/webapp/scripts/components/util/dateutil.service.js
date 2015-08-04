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
    });
}());
