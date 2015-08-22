(function() {
  'use strict';

  angular.module('socialdumpApp')
    .controller('SearchCriteriaController',
      function($scope, SearchCriteria, ParseLinks) {
      $scope.searchCriterias = [];
      $scope.page = 1;
      $scope.loadAll = function() {
        SearchCriteria.query(
          {page: $scope.page, per_page: 20},
          function(result, headers) {
            $scope.links = ParseLinks.parse(headers('link'));
            for (var i = 0; i < result.length; i++) {
              $scope.searchCriterias.push(result[i]);
            }
          });
        };
      $scope.reset = function() {
        $scope.page = 1;
        $scope.searchCriterias = [];
        $scope.loadAll();
      };
      $scope.loadPage = function(page) {
        $scope.page = page;
        $scope.loadAll();
      };
      $scope.loadAll();

      $scope.delete = function(id) {
        SearchCriteria.get({id: id}, function(result) {
          $scope.searchCriteria = result;
          $('#deleteSearchCriteriaConfirmation').modal('show');
        });
      };

      $scope.confirmDelete = function(id) {
        SearchCriteria.delete({id: id},
          function() {
            $scope.reset();
            $('#deleteSearchCriteriaConfirmation').modal('hide');
            $scope.clear();
          });
      };

      $scope.refresh = function() {
        $scope.reset();
        $scope.clear();
      };

      $scope.clear = function() {
        $scope.searchCriteria = {searchCriteria: null, id: null};
      };
    });
}());
