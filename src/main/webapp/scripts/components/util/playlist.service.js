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
        size = data.length,
        //Object that is going to handle the data
        reproducer = reproducerObject.reproducer,
        //Scope that is going to change
        scope = reproducerObject.scope,
        reproduce = function() {
          //cleaning variable
          toReproduce = [];
          if (count < size) {
            //adding new values
            toReproduce.push(
              data.shift(),
              data.shift(),
              data.shift());
            //calling the function handler
            reproducer[reproducerObject.functionHandler](toReproduce, scope);
            //Delay and recursive call in order to
            //make it synchronous
            $timeout(reproduce, 20000);
          }
          count += 3;
        };
        //executing function
        reproduce();
      };
    }]);
}());
