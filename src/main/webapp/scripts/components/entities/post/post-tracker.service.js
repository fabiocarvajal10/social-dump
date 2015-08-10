/**
 * Created by fabio on 7/17/15.
 */
'use strict';
angular.module('socialdumpApp.posts')
  .factory('PostTracker', function($rootScope, $cookies, $http, $q) {
    var stompClient = null,
    subscriber = [],
    listenerPublic = $q.defer(),
    listenerMonitor = $q.defer(),
    connected = $q.defer(),
    currentEvent = null,
    alreadyConnectedOnce = false,
    SOCKET_URL = 'websocket/eventposts',
    SOCKET_BROKER = '/app',
    EVENT_TOPIC = '/topic/eventPublications',
    BLOCK_TOPIC = '/topic/blockPost',
    REMOVE_DESTINATION = '/removePost';
    return {
      connect: function(eventId) {
        //console.log('Connecting');
        currentEvent = eventId;
        var loc = window.location;
        var url = ['//', loc.host,
          loc.pathname, SOCKET_URL]
          .join('');
        console.log(url);
        var socket = new SockJS(url);
        stompClient = Stomp.over(socket);
        var headers = {};
        headers['X-CSRF-TOKEN'] = $cookies[$http.defaults.xsrfCookieName];
        stompClient.connect(headers, function(frame) {
          connected.resolve('success');
          if (!alreadyConnectedOnce) {
            alreadyConnectedOnce = true;
          }
        });
      },

      subscribePublic: function() {
        console.log('into public service');
        connected.promise.then(function() {
          //console.log(currentEvent);
          subscriber.push(stompClient.subscribe([EVENT_TOPIC,
            currentEvent].join('/'),
            function(data) {
              listenerPublic.notify(JSON.parse(data.body));
            }
          ));
        }, null, null);
      },

      subscribeMonitor: function() {
        console.log('into monitoring service');
        connected.promise.then(function() {
          subscriber.push(stompClient.subscribe([BLOCK_TOPIC,
            currentEvent].join('/'),
            function(data) {
              listenerMonitor.notify(JSON.parse(data.body));
            }
          ));
        }, null, null);
      },

      unsubscribe: function() {
        if (subscriber.length > 0) {
          subscriber.forEach(function(element, index) {
            subscriber[index].unsubscribe();
          });
        }
      },

      receivePublic: function() {
        return listenerPublic.promise;
      },

      receiveMonitor: function() {
        return listenerMonitor.promise;
      },

      removePost: function(idToDelete) {
        if (stompClient !== null && subscriber.length > 1) {
          //console.log('Removing PostID: ' + idToDelete);
          var destination = [SOCKET_BROKER + REMOVE_DESTINATION].join('');
          console.log(destination);
          stompClient
            .send(
              destination,
              {},
              JSON.stringify({
                'id': idToDelete,
                'eventId': currentEvent
              })
            );
        }
      },

      disconnect: function() {
        if (stompClient !== null) {
          stompClient.disconnect();
          stompClient = null;
        }
      }
    };
  });
