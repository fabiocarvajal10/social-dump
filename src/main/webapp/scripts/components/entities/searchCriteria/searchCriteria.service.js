'use strict';

angular.module('socialdumpApp')
    .factory('SearchCriteria', function ($resource, DateUtils) {
        return $resource('api/searchCriterias/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
