/**
 * Created by fabio on 7/17/15.
 */
(function() {
	'use strict';
	angular.module('socialdumpApp.posts',[]);
	angular.module('socialdumpApp')
		.config(function($stateProvider) {
			$stateProvider
				.state('public-posts', {
					parent: '',
					url: '/event/:id',
					data: {
						roles: [],
						pageTitle: 'EventPosts'
					},
					views: {
						'content@': {
							templateUrl: 'scripts/app/entities/post/posts.html',
							controller: 'PostController'
						}
					},
					resolve: {
						store: function($ocLazyLoad) {
							return $ocLazyLoad.load({
								name: 'socialdumpApp.posts',
								files: ['scripts/app/entities/post/post.controller.js',
									'scripts/components/entities/post/post.service.js',
									'scripts/components/entities/post/post-tracker.service.js',
									'scripts/components/entities/post/cards.service.js',
									'scripts/components/entities/post/event-public.service.js'
								]
							});
						}
					},
					onEnter:['PostTracker', function(PostTracker) {
						//Loading existing data when entry
						PostTracker.connect();
						PostTracker.subscribe();
					}],
					onExit: function(PostTracker) {
						PostTracker.unsubscribe();
					}
				});
		});
}());

/*.state('posts', {
					abstract: true,
					parent: 'site',
					template: '<ui-view/>',
					url: 'posts',
					data: {
						roles: [],
						pageTitle: 'Posts'
					}
				})
				.state('posts.list', {
					url: '/event/:id',
					data: {
						roles: [],
						pageTitle: 'Posts'
					},
					views: {
						'': {
							templateUrl: 'scripts/app/entities/post/posts.html',
							controller: 'PostController'
						}
					},
					resolve:{
						store: function($ocLazyLoad) {
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
					onEnter:['PostTracker', function(PostTracker) {
						//Loading existing data when entry
						PostTracker.connect();
						PostTracker.subscribe();
					}],
					onExit: function(PostTracker) {
						PostTracker.unsubscribe();
					}
				});
				/*.state('PostDetail', {
					parent: 'entity',
					url: '/post/:id',
					data: {
						roles: [],
						pageTitle: 'Post'
					},
					templateUrl: 'scripts/app/entities/event/event-detail.html',
					controller: 'EventDetailController'
				});*/