/**
 * Created by fabio on 7/17/15.
 */
(function() {
  'use strict';
  angular.module('socialdumpApp.monitor-screen')
    .controller('MonitorScreenController',
      ['$scope', '$stateParams', 'Playlist',
      'PostTracker', 'Cards', 'PostService',
      'EventPublic',
      function($scope, $stateParams, 
        Playlist, PostTracker, Cards, 
        PostService, EventPublic) {
      //This controller uses a websocket
      //connection to receive posts from one event
        var templateUrl = 'scripts/app/entities/monitor/screen/partials/post-cards-monitor.html';
        EventPublic.get({'id': $stateParams.id})
          .$promise.then(function(data) {
            $scope.event = data;
        });

        var reproducerObject = {
          reproducer: Cards,
          scope: $scope,
          template: templateUrl,
          functionHandler: 'createCards'
        };

        $scope.cards = [];
        $scope.filters = [[['tabs',
          'contains',
          'home',
          'twitter',
          'instagram']]];
        $scope.rankers = null;

        $scope.isDropdownOpen = {
          orderBy: false,
          filter: false
        };

        //Loading existing data when entry
        PostService
          .query({'id': $stateParams.id})
          .$promise.then(function(data) {
            //Playlist.play(data, reproducerObject);
            Cards.createCards(data, $scope, templateUrl);
          });

        PostTracker.receivePublic().then(null, null, function(posts) {
          //console.log('Now we are getting posts');
          Playlist.play(posts, reproducerObject);
        });

        PostTracker.receiveMonitor().then(null, null, function(toDelete) {
          //console.log('Now we are getting posts');
          console.log('Removing a post');
          console.log(toDelete.id);

          //Playlist.play(posts, reproducerObject);
        });
        /**
         * Update the filters array based on the given filter
         * $param filter: the name of a tab like 'work'
         */
        $scope.filter = function(filter) {
          $scope.filters = [[['tabs', 'contains', filter]]];
        };

        $scope.isTabActive = function(tab) {
          return $scope.filters && $scope.filters[0][0][2] === tab;
        };

        /**
         * Update the rankers array based on the given ranker
         * $param ranker: the name of a card's property or a custom function
         */
        $scope.orderBy = function(ranker) {
          $scope.rankers = [[ranker, 'asc']];
        };

        $scope.isRankerActive = function(ranker) {
          return $scope.rankers && $scope.rankers[0][0] === ranker;
        };

        /**
         * Delete a given card
         * $param index: the index of the card in the cards array
         */
        $scope.deleteCard = function(id) {
          Cards.deleteCard(id, $scope.cards);
          PostTracker.removePost(id);
        };

        $scope.removeFirstCard = function() {
          Cards.deleteCard($scope.filteredItems[0].id, $scope.cards);
        };

        /**
         * Add a card to the main view
         * Takes a card from the pile of
         *cardsToAdd and prepend it to the list of
         * cards. Take a card belonging to the selected tab
         */
        $scope.addCards = function(cardsToAdd) {
          Cards.addCards($scope.filters, cardsToAdd, $scope.cards);
        };
      }]
    );
})();
