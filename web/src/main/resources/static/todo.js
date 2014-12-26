angular.module('todoApp', ['ngRoute', 'ngResource', 'validation.match'])
    .run(function ($rootScope, $window, $location) {
        $rootScope.$on('$routeChangeStart', function (event, next) {
            if (next.access) {
                var userAuthenticated = $window.sessionStorage.getItem('token');
                if (!userAuthenticated && next.access.loginRequired) {
                    $location.path('/login');
                } else if (userAuthenticated && next.access.logoutRequired) {
                    $location.path('/');
                }
            }
        });
    })

    .config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                redirectTo: '/tasks'
            })
            .when('/tasks', {
                controller: 'TasksCtrl',
                templateUrl: 'partial/tasks.html',
                access: {
                    loginRequired: true
                }
            })
            .when('/login', {
                controller: 'LoginCtrl',
                templateUrl: 'partial/login.html',
                access: {
                    logoutRequired: true
                }

            })
            .when('/createAccount', {
                controller: 'CreateAccountCtrl',
                templateUrl: 'partial/createAccount.html',
                access: {
                    logoutRequired: true
                }
            })
            .otherwise({
                redirectTo: '/'
            });
    })

    .controller('LoginCtrl', ['$scope', '$http', '$window', '$location', function ($scope, $http, $window, $location) {
        $scope.login = function (user) {
            delete $scope.loginError;
            $http.post('', user)
                .success(function (data) {
                    $window.sessionStorage.setItem('token', data.token);
                    $location.path('/');
                })
                .error(function () {
                    $window.sessionStorage.removeItem('token');
                    $scope.loginError = 'Login failed';
                });
        };
        $scope.successLogin = function () {
            $window.sessionStorage.setItem('token', 'swswdw-sws-ws-w-sw-s-w');
            $location.path('/');
        };
    }])

    .controller('CreateAccountCtrl', ['$scope', function ($scope, $window, $location) {

    }])

    .controller('TasksCtrl', ['$scope', '$window', '$location', function ($scope, $window, $location) {
        $scope.todos = [
            {text: 'learn angular', done: true},
            {text: 'build an angular app', done: false}
        ];

        $scope.addTodo = function () {
            $scope.todos.push({text: $scope.todoText, done: false});
            $scope.todoText = '';
        };

        $scope.remaining = function () {
            var count = 0;
            angular.forEach($scope.todos, function (todo) {
                count += todo.done ? 0 : 1;
            });
            return count;
        };

        $scope.archive = function () {
            var oldTodos = $scope.todos;
            $scope.todos = [];
            angular.forEach(oldTodos, function (todo) {
                if (!todo.done) $scope.todos.push(todo);
            });
        };

        $scope.logout = function () {
            $window.sessionStorage.removeItem('token');
            $location.path('/');
        };
    }]);