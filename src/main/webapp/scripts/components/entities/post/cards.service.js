/**
 * Created by fabio on 7/17/15.
 */
'use strict';
angular.module('socialdumpApp.posts')
	.service('Cards', function(){
		/**
		 * Update the filters array based on the given filter
		 * $param filter: the name of a tab like 'work'
		 */

		/**
		 * Delete a given card
		 * $param index: the index of the card in the cards array
		 */
		this.deleteCard = function(id, cards){
			var index = -1;
			for(var i in cards){
				if(cards[i].id == id){
					index = i;
					break;
				}
			}
			if(index !== -1){
				cards.splice(index, 1);
			}
		};


		/**
		 * Add a card to the main view
		 * Takes a card from the pile of cardsToAdd and prepend it to the list of
		 * cards. Take a card belonging to the selected tab
		 */
		this.addCard = function(filters, cardsToAdd, cards){
			var getCurrentTab = function(){
				return filters[0][0][2];
			};

			var getIndexOfNextCardInTab = function(tab){
				var index = -1;

				for(i in cardsToAdd){
					if(cardsToAdd[i].tabs.indexOf(tab) !== -1){
						index = i;
						break;
					}
				}
				return index;
			};

			var index =  getIndexOfNextCardInTab(getCurrentTab());

			if(index !== -1){
				cards.unshift(cardsToAdd[index]);
				cardsToAdd.splice(index, 1);
			}
		}

	});