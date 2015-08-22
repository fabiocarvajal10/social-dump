'use strict';

angular.module('socialdumpApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
