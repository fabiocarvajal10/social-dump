/**
 * Created by fabio on 7/17/15.
 */
(function () {
	'use strict';
	angular.module('socialdumpApp')
		.directive('socialImage', function() {
		  return { 
		  	restrict: 'A',
		  	link: function(scope, elem, attrs) {
	      	var url = attrs.socialImage;
	      	//console.log(url);
	      	elem.css({
	          	'background-image':'url(' + url +')',
	          	'background-repeat': 'no-repeat',
	          	'background-size': '100%'
	      	});
		  	}
		  };
		});
})();