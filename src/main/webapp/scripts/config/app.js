'use strict';

angular.module('socialdumpApp', ['ngAnimate',
  'ngAria', 'ngMessages', 'ngSanitize', 'ngTouch', 'ngStorage',
  'LocalStorageModule', 'ngResource', 'ui.router', 'ngCookies',
  'ngCacheBuster', 'infinite-scroll', 'ui.grid', 'ui.grid.resizeColumns',
  'ui.bootstrap', 'ui.bootstrap.modal', 'ui.utils',
  'ui.load', 'ui.jq', 'oc.lazyLoad', 'pascalprecht.translate', 'ngMaterial',
  'ui.date'/*, 'ui.bootstrap.datetimepicker'*/])
  .controller('AppCtrl', [
    '$scope', '$translate', '$localStorage', '$window',
    function($scope, $translate, $localStorage, $window) {
      // add 'ie' classes to html
      var isIE = !!navigator.userAgent.match(/MSIE/i);
      isIE && angular.element($window.document.body).addClass('ie');
      isSmartDevice($window) && angular.element($window.document.body)
        .addClass('smart');

      // config
      $scope.app = {
        name: 'Angulr',
        version: '2.0.2',
        // for chart colors
        color: {
          primary: '#7266ba',
          info: '#23b7e5',
          success: '#27c24c',
          warning: '#fad733',
          danger: '#f05050',
          light: '#e8eff0',
          dark: '#3a3f51',
          black: '#1c2b36'
        },
        settings: {
          themeID: 1,
          navbarHeaderColor: 'bg-black',
          navbarCollapseColor: 'bg-white-only',
          asideColor: 'bg-black',
          headerFixed: true,
          asideFixed: false,
          asideFolded: false,
          asideDock: false,
          container: false
        }
      };

      // save settings to local storage
      if (angular.isDefined($localStorage.settings)) {
        $scope.app.settings = $localStorage.settings;
      } else {
        $localStorage.settings = $scope.app.settings;
      }
      $scope.$watch('app.settings', function() {
        if ($scope.app.settings.asideDock && $scope.app.settings.asideFixed) {
          // aside dock and fixed must set the header fixed.
          $scope.app.settings.headerFixed = true;
        }
        // for box layout, add background image
        $scope.app.settings.container ? angular.element('html').addClass('bg') :
          angular.element('html').removeClass('bg');
        // save to local storage
        $localStorage.settings = $scope.app.settings;
      }, true);

      function isSmartDevice($window) {
        // Adapted from http://www.detectmobilebrowsers.com
        var ua = $window['navigator']['userAgent'] ||
                 $window['navigator']['vendor'] || $window['opera'];
        // Checks for iOs, Android, Blackberry, Opera Mini, and Windows mobile
        // devices
        return (/iPhone|iPod|iPad|Silk|Android|BlackBerry|Opera Mini|IEMobile/).test(ua);
      }

  }])
  .run(function($rootScope, $location, $window, $http, $state,  Auth, Principal,
                ENV, VERSION) {
    $rootScope.ENV = ENV;
    $rootScope.VERSION = VERSION;
    $rootScope.$on('$stateChangeStart', function(event, toState,
                                                 toStateParams) {
      $rootScope.toState = toState;
      $rootScope.toStateParams = toStateParams;

      if (Principal.isIdentityResolved()) {
        Auth.authorize();
      }

    });

    $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams,
                                                   fromState, fromParams) {
      var titleKey = 'Social Dump';

      $rootScope.previousStateName = fromState.name;
      $rootScope.previousStateParams = fromParams;

      //Set the page title key to the one configured in state or use default one
      if (toState.data.pageTitle) {
        titleKey = toState.data.pageTitle;
      }
      $window.document.title = titleKey;
    });

    $rootScope.back = function() {
      // If previous state is 'activate' or do not exist go to 'home'
      if ($rootScope.previousStateName === 'activate' ||
          $state.get($rootScope.previousStateName) === null) {
        $state.go('home');
      } else {
        $state.go($rootScope.previousStateName, $rootScope.previousStateParams);
      }
    };
  })
  .factory('authInterceptor', function($rootScope, $q, $location,
                                       localStorageService) {
    return {
      // Add authorization token to headers
      request: function(config) {
        config.headers = config.headers || {};
        var token = localStorageService.get('token');

        if (token && token.expires && token.expires > new Date().getTime()) {
          config.headers['x-auth-token'] = token.token;
        }

        return config;
      }
    };
  })
  .config(function($stateProvider, $urlRouterProvider, $httpProvider,
                   $locationProvider,
                   httpRequestInterceptorCacheBusterProvider) {

    //Cache everything except rest api requests
    httpRequestInterceptorCacheBusterProvider
      .setMatchlist([/.*api.*/, /.*protected.*/], true);

    $urlRouterProvider.otherwise('/');
    $stateProvider.state('site', {
      'abstract': true,
      views: {
        'navbar@': {
          templateUrl: 'scripts/components/navbar/navbar.html',
          controller: 'NavbarController'
        }
      },
      resolve: {
        authorize: ['Auth',
          function(Auth) {
            return Auth.authorize();
          }
        ]
      }
    });

    $httpProvider.interceptors.push('authInterceptor');

  })
  .config(
  ['JQ_CONFIG', 'MODULE_CONFIG',
    function(JQ_CONFIG, MODULE_CONFIG) {

      function load(srcs, callback) {
        return {
          deps: ['$ocLazyLoad', '$q',
            function($ocLazyLoad, $q) {
              var deferred = $q.defer();
              var promise = false;
              srcs = angular.isArray(srcs) ? srcs : srcs.split(/\s+/);
              if (!promise) {
                promise = deferred.promise;
              }
              angular.forEach(srcs, function(src) {
                console.log(src);
                promise = promise.then(function() {
                  if (JQ_CONFIG[src]) {
                    return $ocLazyLoad.load(JQ_CONFIG[src]);
                  }
                  angular.forEach(MODULE_CONFIG, function(module) {
                    if (module.name === src) {
                      name = module.name;
                    } else {
                      name = src;
                    }
                  });
                  return $ocLazyLoad.load(name);
                });
              });
              deferred.resolve();
              return callback ? promise.then(function() {
                                              return callback(); }) :
                                promise;
            }]
        };
      }


  }]);

