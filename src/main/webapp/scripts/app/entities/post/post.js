/**
 * Created by fabio on 7/17/15.
 */
'use strict';
angular.module('socialdumpApp.posts',[]);
angular.module('socialdumpApp')
	.config(function($stateProvider) {
		$stateProvider
			.state('posts', {
				abstract: true,
				parent: 'home',
				template: '<ui-view/>',
				url: 'posts',
				data: {
					roles: ['ROLE_USER'],
					pageTitle: 'Posts'
				}
			})
			.state('posts.list', {
				url: '',
				data: {
					roles: ['ROLE_USER'],
					pageTitle: 'Posts'
				},
				views: {
					'': {
						templateUrl: 'scripts/app/entities/post/posts.html',
						controller: 'PostController'
					}
				},
				resolve:{
					store: function($ocLazyLoad){
						console.log("here")
						return $ocLazyLoad.load({
							name: 'socialdumpApp.posts',
							files: ['scripts/app/entities/post/post.controller.js',
								'scripts/components/entities/post/post.service.js',
								'scripts/components/entities/post/post-tracker.service.js',
								'scripts/components/entities/post/cards.service.js'
							]
						})
					}
				},
				onEnter: function(PostTracker) {
					PostTracker.connect();
					console.log("Subscribing");
					PostTracker.subscribe();
				},
				onExit: function(PostTracker) {
					PostTracker.unsubscribe();
				}
			});
			/*.state('PostDetail', {
				parent: 'entity',
				url: '/post/:id',
				data: {
					roles: ['ROLE_USER'],
					pageTitle: 'Post'
				},
				templateUrl: 'scripts/app/entities/event/event-detail.html',
				controller: 'EventDetailController'
			});*/
	});
