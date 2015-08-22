/**
 * Created by fabio on 7/17/15.
 */
'use strict';
angular.module('socialdumpApp.posts')
  .factory('MonitorPostTracker', function($rootScope, $cookies, $http, $q) {
    var stompClient = null,
    subscriber = null,
    listener = $q.defer(),
    connected = $q.defer(),
    currentEvent = null,
    alreadyConnectedOnce = false,
    SOCKET_URL = 'websocket/eventposts',
    REMOVE_TOPIC = '/topic/removePost';
    return {
      connect: function() {
        //console.log('Connecting');
        var loc = window.location;
        var url = ['//', loc.host,
          loc.pathname, 'websocket/eventposts']
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

      subscribe: function() {
        console.log('into service');
        currentEvent = eventId;
        connected.promise.then(function() {
          subscriber = stompClient.subscribe(REMOVE_TOPIC),
            function(data) {
              listener.notify(JSON.parse(data.body));
            }
          );
        }, null, null);
      },

      unsubscribe: function() {
        if (subscriber !== null) {
          subscriber.unsubscribe();
        }
      },

      receive: function() {
        return listener.promise;
      },

      removePost: function(idToDelete) {
        if (stompClient !== null && subscriber !== null) {
          console.log('Removing PostID: ' + idToDelete);
          stompClient
            .send(REMOVE_TOPIC,
            {},
            JSON.stringify({
              'id': idToDelete,
              'eventId': currentEvent
            }));
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
