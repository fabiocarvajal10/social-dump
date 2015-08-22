/**
 * Created by Franz on 13/08/2015.
 */
(function() {
  'use strict';
  angular.module('socialdumpApp')
    .factory('UtilInterceptorService',
    ['$modal', 'NoInternetHtmlFactory',
     function($modal, NoInternetHtmlFactory) {
       return {
         'openNoInternet': function () {
           var modalInstance = $modal.open({
             animation: true,
             template: NoInternetHtmlFactory.noInternet(),
             controller: 'ModalDetailInterceptorCtrl',
             resolve: {
              'message': function(){
                return '';
              }
             }
           });

           modalInstance.result.then(function () {

           }, function () {

           });
         },

         'open': function (message) {
           var modalInstance = $modal.open({
             animation: true,
             templateUrl: 'scripts/components/interceptors/partials/message.modal.html',
             controller: 'ModalDetailInterceptorCtrl',
             resolve: {
              'message': function() {
                return message;
              }
             }
           });

           modalInstance.result.then(function () {

           }, function () {

           });
         },
       }
     }]
  );
})();
