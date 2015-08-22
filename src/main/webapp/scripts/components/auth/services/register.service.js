'use strict';

angular.module('socialdumpApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


