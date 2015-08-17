/**
 * Created by Franz on 13/08/2015.
 */
angular.module('socialdumpApp')
  .config(['$httpProvider', function($httpProvider){
    $httpProvider.interceptors.push(function($q, $injector){
      return {
        'responseError': function(rejection) {
          var modal = $injector.get('UtilInterceptorService');
          var modalStack = $injector.get('$modalStack');
          var status = rejection.status;
          var createMessage = function(title, body) {
            return {
              'title': title,
              'body': body
            }
          }

          switch(status) {
            case 0:
                  modal.openNoInternet();
                  break;
            case 500:
                  modalStack.dismissAll('cancel');
                  modal.open(createMessage('Error al completar su solicitud',
                    'Algo ha salido mal. En este momento no se puede completar su solicitud. ' +
                    'Por favor inténtelo después'));
                  break;
            default:
                  break;
          }

          return $q.reject(rejection);
        }
      };
    });
  }]);
