/**
 * Created by Franz on 13/08/2015.
 */
(function() {
  'use strict';
  angular.module('socialdumpApp')
    .factory('NoInternetHtmlFactory', function(){
      var noInternetTemplate =
        ' <div class="modal-header"> ' +
        '   <h4 class="modal-title m-n font-thin h3"> ' +
        '     No se detecta conexión a internet ' +
        '   </h4> ' +
        ' </div> ' +
        ' <div class="modal-body"> ' +
        '   <h3> Por favor compruebe su conexión a internet e inténtelo después </h3> ' +
        ' </div> ' +
        ' <div class="modal-footer"> ' +
        ' <button class="btn btn-warning" ng-click="cancel()"> Aceptar </button> ' +
        ' </div> ';
      return {
        'noInternet': function () {
          return noInternetTemplate;
        }
      }
    });
})();
