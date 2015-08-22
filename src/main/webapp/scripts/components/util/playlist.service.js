/**
 * Created by fabio on 8/9/15.
 */
(function() {
  'use strict';
  angular.module('socialdumpApp.posts')
    .service('Playlist', ['$timeout', function($timeout) {
      //@data that is going to be displayed
      //@reproduceObject contains the $scope
      //that is going to be changed
      //the Object that is going to do task
      //and the functionHandler
      this.play = function(data, reproducerObject) {
        //console.log('Reproducing');
        var toReproduce,
        count = 0,
        jump = 10,
        size = data.length,
        //Object that is going to handle the data
        reproducer = reproducerObject.reproducer,
        //Scope that is going to change
        scope = reproducerObject.scope,
        //Template
        template = reproducerObject.template,
        reproduce = function() {
          //cleaning variable
          toReproduce = [];
          if (count < size) {
            //adding new values
            for (var i = 0; i < jump; i++) {
              toReproduce.push(data.shift());
            }
            //calling the function handler
            reproducer[reproducerObject.functionHandler](toReproduce,
              scope, template);
            //Delay and recursive call in order to
            //make it synchronous
            count += jump;
            $timeout(reproduce, 20000);
          }
        };
        //executing function
        reproduce();
      };
    }]);
}());
