/**
 * Created by fabio on 7/17/15.
 */
'use strict';

angular.module('socialdumpApp.posts')
	.controller('PostController',
		['$scope', 'PostTracker', 'Cards',
			function ($scope, PostTracker, Cards) {
			//This controller uses a websocket connection to receive posts from one event

				$scope.eventPosts = [];

				$scope.cards = [];

				$scope.filters = [[['tabs', 'contains', 'home', 'twitter', 'instagram']]];
				$scope.rankers = null;

				$scope.isDropdownOpen = {
					orderBy: false,
					filter: false
				};


				PostTracker.receive().then(null, null, function(posts) {
					console.log('Receive');
					var cardsToAdd = [];
					var card = {};
					posts.forEach( function(post) {
						card = $scope.createCard(post);
						cardsToAdd.push(card);
					});
					$scope.addCards(cardsToAdd);
				});




				$scope.createCard = function(post){
					//console.log(post);
					var socialNetworkName = post.socialNetworkName.toLowerCase();
					return {
						'id': post.id,
						'template': 'scripts/app/entities/post/partials/post-card.html',
						'tabs': ['home', socialNetworkName],
						'social': socialNetworkName,
						'event': post.eventName,
						'searchCriteria': post.searchCriteria,
						'body': post.body
					};
				};

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
					Cards.addCard($scope.filters, cardsToAdd, $scope.cards)
				};

				$scope.cards.push({
					id: 2,
					template : "scripts/app/entities/post/partials/post-card.html",
					tabs : ["home", "instagram"],
					"position" : "Data Scientist",
					"company" : "Big Data Inc."
				});







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

				 /*addCards(
				 [{
				 id: 1,
				 template: 'scripts/app/entities/post/partials/post-card.html',
				 tabs: 'Facebook',
				 social: 'Facebook',
				 event: '#NoEraPenal',
				 searchCriteria: 'NoEraPenal',
				 body: 'Mexico ladrones #NoEraPenal'
				 }]
				 );
				 */

			}
		]
	);