(function() {
  'use strict';

  angular.module('socialdumpApp').controller('EventDialogController', [
    '$rootScope', '$scope', '$stateParams', '$modalInstance', 'entity', 'Event',
    'EventType', 'SearchCriteria', 'DateUtils',
    'SocialNetwork', 'SocialNetworkIcon',
    function($rootScope, $scope, $stateParams, $modalInstance, entity,
             Event, EventType, SearchCriteria, DateUtils,
             SocialNetwork, SocialNetworkIcon) {
      $scope.event = entity;
      $scope.hours = DateUtils.halfHoursOfTheDay24Format();
      $scope.event.postDelay = 60;
      $scope.sameHashtag = true;
      $scope.todayMidnight = DateUtils.todayMidnight();
      $scope.minStartDate = new Date(DateUtils.todayMidnight().getTime() - 1);
      $scope.socialNetworksDict = {};
      $scope.event.searchCriterias = [];

      $scope.hstep = 1;
      $scope.mstep = 15;
      $scope.isMeridian = true;

      var initDateTime = function() {
        var today = DateUtils.toNextHalfHour(new Date());
        $scope.event.startDate = today;
        // $scope.event.startTime = DateUtils.toHourMinutesString(today);
        $scope.event.endDate = DateUtils.addDays(today, 1);
          // new Date(today.getTime() + 24 * 60 * 60 * 1000);
        // $scope.event.endTime = $scope.event.startTime;
      };
      initDateTime();

      // $scope.changeStartTime = function(timeStr) {
      //   $scope.event.startDate.setHours(DateUtils.hoursOfString(timeStr));
      //   $scope.event.startDate.setMinutes(DateUtils.minutesOfString(timeStr));
      // };

      // $scope.changeEndTime = function(timeStr) {
      //   $scope.event.endDate.setHours(DateUtils.hoursOfString(timeStr));
      //   $scope.event.endDate.setMinutes(DateUtils.minutesOfString(timeStr));
      // };

      $scope.eventTypes = EventType.query();

      $scope.socialNetworks = SocialNetwork.query(function() {
        for (var i = 0; i < $scope.socialNetworks.length; i++) {
          $scope.socialNetworksDict[$scope.socialNetworks[i].id] =
            $scope.socialNetworks[i];
          $scope.socialNetworks[i].selected = true;
          SocialNetworkIcon.iconForSocialNetwork($scope.socialNetworks[i]);
        }
      });

      $scope.load = function(id) {
        Event.get({id: id}, function(result) {
          $scope.event = result;
          $scope.minStartDate = ($scope.event.status.status === 'Pendiente') ?
            $scope.todayMidnight : null;
        });
      };

      var onSaveFinished = function(result) {
        // var searchCriteria = {
        //   eventId: result.id,
        //   searchCriteria: $scope.event.description
        // };
        // for (var i = 0; i < $scope.socialNetworks.length; i++) {
        //   if (!!$scope.socialNetworks[i].selected) {
        //     searchCriteria.socialNetworkId = $scope.socialNetworks[i].id;
        //     SearchCriteria.save(searchCriteria);
        //   }
        // }
        $scope.$emit('socialdumpApp:eventUpdate', result);
        $rootScope.$broadcast('socialdumpApp:eventAdded', result);
        $modalInstance.close(result);
      };

      $scope.save = function() {
        if ($scope.event.id !== null) {
          Event.update($scope.event, onSaveFinished);
        } else {
          $scope.event.organizationId = $rootScope.currentOrg.id;
          Event.save($scope.event, onSaveFinished);
        }
      };

      $scope.clear = function() {
        $modalInstance.dismiss('cancel');
      };

    }]);
}());
