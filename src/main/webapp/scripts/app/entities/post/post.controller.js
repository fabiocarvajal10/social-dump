/**
 * Created by fabio on 7/17/15.
 */
(function () {
	'use strict';
	angular.module('socialdumpApp.posts')
	  .controller('PostController',
	   	['$scope', '$timeout', '$stateParams', 
	   	'PostTracker', 'Cards', 'PostService', 'EventPublic',
			function ($scope, $timeout, $stateParams, 
				PostTracker, Cards, PostService, EventPublic) {
			//This controller uses a websocket connection to receive posts from one event

				EventPublic.get({'id': $stateParams.id})
					.$promise.then(function(data) {
						$scope.event = data;
				});

				$scope.cards = [];
				$scope.filters = [[['tabs', 'contains', 'home', 'twitter', 'instagram']]];
				$scope.rankers = null;

				$scope.isDropdownOpen = {
					orderBy: false,
					filter: false
				};

				//Loading existing data when entry
				PostService
					.query( {'id': $stateParams.id} )
					.$promise.then(function(data) {
						play(data)
						//Cards.createCards(data, $scope);
					});
				
				//playlist continuar con esto mas tarde
				function play(data) {
					var toReproduce,
					count = 1,
					size = data.length,
					reproduce = function() {
						//cleaning variable
						toReproduce = []
						if(count < size) {
							//adding new values
							toReproduce.push(
								data.shift(), 
								data.shift(), 
								data.shift());
							//calling service that creates de cards
							Cards.createCards(toReproduce, $scope)
							//Delay and recursive call in order to
							//make it synchronous
							$timeout(reproduce, 5000);
						}
						count+=3;
					}
					//executing function
					reproduce();
				}
				
				PostTracker.receive().then(null, null, function(posts) {
					//console.log('Now we are getting posts')	
					play(posts)
				});

				/**
				 * Update the filters array based on the given filter
				 * $param filter: the name of a tab like 'work'
				 */
				$scope.filter = function(filter){
					$scope.filters = [[['tabs', 'contains', filter]]];
				};

				$scope.isTabActive = function(tab){
					return $scope.filters && $scope.filters[0][0][2] === tab;
				};

				/**
				 * Update the rankers array based on the given ranker
				 * $param ranker: the name of a card's property or a custom function
				 */
				$scope.orderBy = function(ranker){
					$scope.rankers = [[ranker, "asc"]];
				};

				$scope.isRankerActive = function(ranker){
					return $scope.rankers && $scope.rankers[0][0] === ranker;
				};

				/**
				 * Delete a given card
				 * $param index: the index of the card in the cards array
				 */
				$scope.deleteCard = function(id){
					Cards.deleteCard(id, $scope.cards);
				};

				$scope.removeFirstCard = function(){
					Cards.deleteCard($scope.filteredItems[0].id, $scope.cards);
				};

				/**
				 * Add a card to the main view
				 * Takes a card from the pile of cardsToAdd and prepend it to the list of
				 * cards. Take a card belonging to the selected tab
				 */
				$scope.addCards = function(cardsToAdd){
					Cards.addCards($scope.filters, cardsToAdd, $scope.cards)
				};

				//Refactorizar pasar a servicio
				/*

				 PostTracker.receive().then(null, null, function(posts) {
				  console.log('Receive');
				  console.log(posts);
				 });

				 $scope.createCard = function(post){
				 console.log(post);
				 return {
				 id: post.id,
				 template: 'scripts/app/entities/post/partials/post-card.html',
				 tabs: post.socialNetworkName,
				 social: post.socialNetworkName,
				 event: post.eventName,
				 searchCriteria: post.searchCriteria,
				 body: post.body
				 };
				 };
				}
				*/
				}
			]
		);
})();