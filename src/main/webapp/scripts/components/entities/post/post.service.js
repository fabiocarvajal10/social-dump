/**
 * Created by fabio on 7/17/15.
 */

angular.module('socialdumpApp')
	.factory('PostTracker', function ($rootScope, $cookies, $http, $q) {
		var stompClient = null;
		var subscriber = null;
		var listener = $q.defer();
		var connected = $q.defer();
		var alreadyConnectedOnce = false;

		return {
			connect: function () {
				console.log("Connecting");
				var loc = window.location;
				var url = '//' + loc.host + loc.pathname + 'websocket/eventposts';
				var socket = new SockJS(url);
				stompClient = Stomp.over(socket);
				var headers = {};
				headers['X-CSRF-TOKEN'] = $cookies[$http.defaults.xsrfCookieName];
				stompClient.connect(headers, function(frame) {
					connected.resolve("success");
					if (!alreadyConnectedOnce) {
						$rootScope.$on('$stateChangeStart', function (event) {
							//sendActivity();
						});
						alreadyConnectedOnce = true;
					}
				});
			},
			subscribe: function() {
				console.log("into service")
				connected.promise.then(function() {
					subscriber = stompClient.subscribe("/topic/eventPublications", function(data) {
						console.log(data.body);
						listener.notify(JSON.parse(data.body));
					});
				}, null, null);
			},
			unsubscribe: function() {
				if (subscriber != null) {
					subscriber.unsubscribe();
				}
			},
			receive: function() {
				return listener.promise;
			},
			sendActivity: function () {
				if (stompClient != null) {
					sendActivity();
				}
			},
			disconnect: function() {
				if (stompClient != null) {
					stompClient.disconnect();
					stompClient = null;
				}
			}
		};
	});