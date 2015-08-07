(function() {
  'use strict';

  angular.module('socialdumpApp')
    .service('SocialNetworkIcon', function() {

      /**
      * Devuelve el ícono de una red social, dado su nombre.
      */
      this.iconForSocialNetworkName = function(socialNetworkName) {
        var result = '';
        switch (socialNetworkName) {
          case 'Facebook':
            result =
              '<span class="fa-stack fa-lg facebook-color">' +
                '<i class="fa fa-circle fa-stack-2x"></i>' +
                '<i class="fa fa-facebook fa-stack-1x text-white"></i>' +
              '</span>';
            break;
          case 'Twitter':
            result =
              '<span class="fa-stack fa-lg twitter-color">' +
                '<i class="fa fa-circle fa-stack-2x"></i>' +
                '<i class="fa fa-twitter fa-stack-1x text-white"></i>' +
              '</span>';
            break;
          case 'Instagram':
            result =
               '<span class="fa-stack fa-lg instagram-color">' +
                 '<i class="fa fa-circle fa-stack-2x"></i>' +
                 '<i class="fa fa-instagram fa-stack-1x text-white"></i>' +
               '</span>';
            break;
        }
        return result;
      };

      /**
      * Agrega el ícono a un objeto red social.
      */
      this.iconForSocialNetwork = function(socialNetwork) {
        socialNetwork.icon = this.iconForSocialNetworkName(socialNetwork.name);
      };
    });
}());
