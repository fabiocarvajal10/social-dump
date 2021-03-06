/**
 * Created by fabio on 7/17/15.
 */
'use strict';
angular.module('socialdumpApp.posts')
  .service('Cards', function() {
    this.createCards = function(posts, externalScope, template) {
      var cardsToAdd = [],
        card = {};
      posts.forEach(function(post) {
        card = createCard(post, template);
        cardsToAdd.push(card);
      });
      this.addCards(cardsToAdd, externalScope);
    };

    var createCard = function(post, template) {
      var socialNetworkName = post.socialNetworkName.toLowerCase();
      return {
        'id': post.id,
        'template': template,
        'tabs': ['home', socialNetworkName],
        'data': {
          'social': socialNetworkName,
          'socialId': post.socialNetworkId,
          'event': post.eventName,
          'searchCriteria': post.searchCriteria,
          'body': post.body,
          'snUser': post.snUserEmail,
          'snUserId': post.snUserId,
          'createdAt': post.createdAt,
          'eventId': post.eventId,
          'mediaUrl': post.mediaUrl,
          'profileImage': post.profileImage,
          'profileUrl': post.profileUrl,
          'fullName': post.fullName
        }
      };
    };
    /**
     * Takes cardsToAdd and prepend it to the list of
     * cards.
     */
    this.addCards = function(cardsToAdd, externalScope) {
      cardsToAdd.forEach(function(card, index) {
        if (index !== -1) {
          externalScope.cards.unshift(card);
          cardsToAdd.splice(index, 1);
        }
      });
    };

    /**
     * Delete a given card
     * $param index: the index of the card in the cards array
     */
    this.deleteCard = function(id, cards) {
      var index = -1;
      for (var i in cards) {
        if (cards[i].id === id) {
          index = i;
          break;
        }
      }
      if (index !== -1) {
        cards.splice(index, 1);
      }
    };
  });
