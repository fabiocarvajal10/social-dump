(function() {
  'use strict';

  angular.module('socialdumpApp')
    .service('SocialNetworkIcon', function() {

      /**
      * Devuelve el ícono de una red social, dado su nombre.
      */
      this.iconForSocialNetworkName = function(socialNetworkName) {
        var socialNetLow = socialNetworkName.toLowerCase();
        return '<span class="fa-stack fa-lg ' + socialNetLow + '-color">' +
               '<i class="fa fa-circle fa-stack-2x"></i>' +
               '<i class="fa fa-' + socialNetLow +
               ' fa-stack-1x text-white"></i></span>';
      };

      /**
      * Agrega el ícono a un objeto red social.
      */
      this.iconForSocialNetwork = function(socialNetwork) {
        socialNetwork.icon = this.iconForSocialNetworkName(socialNetwork.name);
      };
    });
}());
