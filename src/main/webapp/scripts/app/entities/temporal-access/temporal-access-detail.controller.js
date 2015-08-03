/**
 * Created by Franz on 27/07/2015.
 */
'use strict';

angular.module('socialdumpApp.temporalAccess')
  .controller('TemporalAccessDetailCtrl',
    function ($scope, TemporalAccessService, $modalInstance, MonitorService, gridTA, gridRow) {

      $scope.temporalAccess = {};
      $scope.monitorContacts = [];
      $scope.errorMessage = '';

      $scope.init = function(){
        if(gridRow !== null){
          $scope.temporalAccess = gridRow.entity;
        }
      };

      $scope.getOptions = function(){
        MonitorService.getAll('')
          .then(function(data){
            $scope.monitorContacts = data;
            $scope.temporalAccess.allEvent = true;
          })
          .catch(function(){

          });
      }

      $scope.createTA = function(){
        TemporalAccessService.register($scope.temporalAccess)
        .then(function(data){
          gridTA.data.push(data);
          $modalInstance.close();
        })
        .catch(function(error){
          $scope.errorMessage = error;
        });
      }

      $scope.deleteTA = function(temporalAccessId) {
        TemporalAccessService.delete(temporalAccessId)
        .then(function(data){
          var index = gridTA.data.indexOf(gridRow);
          gridTA.data.splice(index, 1);
          $modalInstance.close();
        })
        .catch(function(error){
          $scope.errorMessage = error;
        });
      };

      $scope.ok = function(){
        $modalInstance.close();
      };

      $scope.cancel = function(){
        $modalInstance.dismiss('cancel');
      };

      $scope.init();
  });
