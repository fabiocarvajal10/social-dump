/**
 * Created by fabio on 7/17/15.
 */
(function() {
  'use strict';
  angular.module('socialdumpApp.monitor-screen')
    .controller('MonitorScreenController',
      ['$scope', '$stateParams', 'Playlist',
      'PostTracker', 'Cards', 'PostService',
      'EventPublic', 'toaster', '$modal', 'OwnerService',
      function($scope, $stateParams,
        Playlist, PostTracker, Cards,
        PostService, EventPublic, toaster, $modal, OwnerService) {
      /**This controller uses a websocket
      *connection to receive posts from one event
      **/
        //Template for the cards of the monitor
        var templateUrl = 'scripts/app/entities/monitor/screen/partials/post-cards-monitor.html';

        OwnerService.validate($stateParams.id);

        //Get the data of the event
        EventPublic.get({'id': $stateParams.id})
          .$promise.then(function(data) {
            $scope.event = data;
        });

        /**Object that is going to have
        *data that is going to be reproduce by the
        *"Playlist" service
        **/
        var reproducerObject = {
          reproducer: Cards,
          scope: $scope,
          template: templateUrl,
          functionHandler: 'createCards'
        };

        //Stores the cards that are going to be displayed
        $scope.cards = [];
        $scope.filters = [[['tabs',
          'contains',
          'home',
          'twitter',
          'instagram']]];
        $scope.rankers = null;

        //Checks if a post is deleted
        $scope.deleting = false;

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

        /**Websocket listener response
        *callback: displays all the posts and send the info
        *to the Playlist service
        **/
        PostTracker.receivePublic().then(null, null, function(posts) {
          //console.log('Now we are getting posts');
          Playlist.play(posts, reproducerObject);
        });

        //Tracks the post that is going to be deleted
        PostTracker.receiveMonitor().then(null, null, function(toDelete) {
          //console.log('Now we are getting posts');
          //console.log('Removing a post');
          //console.log(toDelete.id);
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
          PostTracker.removePost(id);
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

        //Cancels the deleting of a post
        $scope.cancel = function() {
          $scope.deleted === false;
        };

        /**Executes when removing a post
        *Receives the id of the post that is going to be deleted
        **/
        $scope.onRemoveCard = function(id) {
          if ($scope.deleting === false) {
            var currentCard = null;
            $scope.cards.forEach(function(card, index, array) {
              if (id === card.id) {
                currentCard = card;
                array.splice(index, 1);
              }
            });

            $scope.deleting = true;

            toaster.pop(
              {
                type: 'warning',
                title: 'Deshacer eliminación',
                body: 'Click para deshacer',
                timeout: 10000,
                bodyOutputType: 'trustedHtml',
                id: '',
                showCloseButton: false,
                clickHandler: function(element) {
                  toasterClickHandler(currentCard);
                },
                onHideCallback: function() {
                  closeToaster(id);
                }
              }
            );
          }
        };

        var toasterClickHandler = function(restoreCard) {
            $scope.deleting = false;
            $scope.cards.unshift(restoreCard);
            toaster.clear();
          };

        var closeToaster = function(id) {
          if ($scope.deleting === true) {
            $scope.deleteCard(id);
            toaster.pop({
              title: 'Eliminado',
              body: '',
              timeout: 125
            });
            $scope.deleting = false;
          }
        };

        /**
         *Opens a confirmation modal. Takes the search criteria
         * to be unsync
         */
        $scope.openUnsyncModal = function(searchCriteria) {
          var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'scripts/app/entities/monitor/screen/partials/unsync.modal.html',
            controller: 'UnsyncDetailController',
            resolve: {
              'event': function() {
                return $scope.event;
              },
              'searchCriteria': function() {
                return searchCriteria;
              }
            }
          });

          modalInstance.result.then(function() {

          }, function() {

          });

          $scope.init();
        };
      }]
    );
})();

