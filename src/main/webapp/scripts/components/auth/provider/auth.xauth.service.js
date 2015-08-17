'use strict';

angular.module('socialdumpApp')
    .factory('AuthServerProvider', function loginService($http, localStorageService, Base64,
          AccountService, $q, Principal) {
        return {
            login: function(credentials) {
                var data = "username=" + credentials.username + "&password="
                    + credentials.password;
                return $http.post('api/authenticate', data, {
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded",
                        "Accept": "application/json"
                    }
                }).success(function (response) {
                    localStorageService.set('token', response);
                    AccountService.getUserId()
                    .then(function(data){
                      localStorageService.set('userId', data);
                    });
                    return response;
                });
            },

            access: function(credentials) {
              var cred = "id=" + credentials.id + "&username=" + credentials.username + "&password="
                       + credentials.password;
              var q = $q.defer();
              $http({
                headers: {
                  "Content-Type": "application/x-www-form-urlencoded",
                  "Accept": "*/*"
                },
                url: 'api/access',
                method: 'POST',
                data: cred
              }).
              success(function(data) {
                Principal.identity(true);
                localStorageService.set('token', data);
                q.resolve(data);
              }).
              catch(function(error) {
                if(error.data === 'Monitor cant access before defined time'){
                  error = 'El acceso temporal aún no inicia';
                }else if(error.data == 'Monitor cant access after defined time'){
                  error = 'El acceso temporal ha finalizado';
                }else if(error.data.error = 'Unauthorized'){
                  error = 'Usuario o contraseña no válido';
                }else{
                  error = 'Error inesperado al intentar acceder';
                }
                q.reject(error);
              });

              return q.promise;
          },
            logout: function() {
                //Stateless API : No server logout
                localStorageService.clearAll();
            },
            getToken: function () {
                return localStorageService.get('token');
            },
            hasValidToken: function () {
                var token = this.getToken();
                return token && token.expires && token.expires > new Date().getTime();
            }
        };
    });
