(function() {
  'use strict';

  angular.module('socialdumpApp').controller('EventDialogController', [
    '$scope', '$stateParams', '$modalInstance', 'entity', 'Event',
    'EventType', 'SearchCriteria', 'OrganizationService', 'DateUtils',
    'SocialNetwork', 'SocialNetworkIcon',
    function($scope, $stateParams, $modalInstance, entity, Event,
             EventType, SearchCriteria, OrganizationService, DateUtils,
             SocialNetwork, SocialNetworkIcon) {
      $scope.event = entity;
      $scope.hours = DateUtils.halfHoursOfTheDay24Format();
      $scope.event.postDelay = 60;
      $scope.Date = function(date) {
        return new Date(date);
      };
      var initDateTime = function() {
        var today = DateUtils.toNextHalfHour(new Date());
        $scope.event.startDate = today;
        $scope.event.startTime = DateUtils.toHourMinutesString(today);
        $scope.event.endDate =
          new Date(today.getTime() + 24 * 60 * 60 * 1000);
        $scope.event.endTime = $scope.event.startTime;
      };
      initDateTime();

      $scope.changeStartTime = function(timeStr) {
        $scope.event.startDate.setHours(DateUtils.hoursOfString(timeStr));
        $scope.event.startDate.setMinutes(DateUtils.minutesOfString(timeStr));
      };

      $scope.changeEndTime = function(timeStr) {
        $scope.event.endDate.setHours(DateUtils.hoursOfString(timeStr));
        $scope.event.endDate.setMinutes(DateUtils.minutesOfString(timeStr));
      };

      $scope.eventTypes = EventType.query();

      $scope.socialNetworksDict = {};

      $scope.socialNetworks = SocialNetwork.query(function() {
        console.log(JSON.stringify($scope.socialNetworks));
        for (var i = 0; i < $scope.socialNetworks.length; i++) {
          $scope.socialNetworksDict[$scope.socialNetworks[i].name] =
            $scope.socialNetworks[i];
          SocialNetworkIcon.iconForSocialNetwork($scope.socialNetworks[i]);
        }
      });

      $scope.load = function(id) {
        Event.get({id: id}, function(result) {
          $scope.event = result;
        });
      };

      var onSaveFinished = function(result) { // TODO
        var searchCriteria = {
          eventId: result.id,
          statusId: 1,
          searchCriteria: $scope.hashtag
        };
        for (var i = 0; i < $scope.socialNetworks.length; i++) {
          if (!!$scope.socialNetworks[i].selected) {
            searchCriteria.socialNetworkId = $scope.socialNetworks[i].id;
            SearchCriteria.save(searchCriteria);
          }
        }
        $scope.$emit('socialdumpApp:eventUpdate', result);
        $modalInstance.close(result);
      };

      $scope.save = function() {
        if ($scope.event.id !== null) {
          Event.update($scope.event, onSaveFinished);
        } else {
          $scope.event.organizationId = OrganizationService.getCurrentOrgId();
          Event.save($scope.event, onSaveFinished);
        }
      };

      $scope.clear = function() {
        $modalInstance.dismiss('cancel');
      };
    }]);
}());
