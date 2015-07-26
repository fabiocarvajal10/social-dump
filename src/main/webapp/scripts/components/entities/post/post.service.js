/**
 * Created by fabio on 7/25/15.
 */
angular.module('socialdumpApp.posts')
	.service('PostService',[
		'$resource', 'DateUtils',
		function($resource, DateUtils) {
			return $resource('api/social-network-posts/:id', {}, {
				'query': { method: 'GET', isArray: true},
				'get': {
					method: 'GET',
					transformResponse: function(data) {
						data = angular.fromJson(data);
						data.startDate =
							DateUtils.convertLocaleDateFromServer(data.startDate);
						data.endDate =
							DateUtils.convertLocaleDateFromServer(data.endDate);
						data.activatedAt =
							DateUtils.convertLocaleDateFromServer(data.activatedAt);
						return data;
					}
				}
			});
		}
	]);