angular.module('todoApp', ['ngRoute', 'ngResource', 'validation.match'])
    .run(function ($rootScope, $window, $location, $http) {
        $rootScope.$on('$routeChangeStart', function (event, next) {
            if (next.access) {
                var userAuthenticated = $window.localStorage.getItem('token');
                if (!userAuthenticated && next.access.loginRequired) {
                    $location.path('/login');
                } else if (userAuthenticated && next.access.logoutRequired) {
                    $location.path('/');
                }
            }
        });
        $http.defaults.headers.common.Accept = 'application/json'
        $http.defaults.headers.common['Content-Type'] = 'application/json'
    })

    .config(function ($routeProvider, $httpProvider) {
        $httpProvider.interceptors.push(function ($window, $q) {
            return {
                request: function (config) {
                    var token = $window.localStorage.getItem('token');
                    if (token) {
                        config.headers = config.headers || {};
                        config.headers['X-Auth-Token'] = token;
                    }
                    return config;
                },
                responseError: function (rejection) {
                    if (rejection.status === 401) {
                        logout();
                    }
                    return $q.reject(rejection);
                }
            };
        });

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
            $('#loginButton').button('loading');
            delete $scope.loginError;
            user.password = CryptoJS.SHA256($scope.password).toString(CryptoJS.enc.Hex);
            $http.post(APIConfig.authentication, user)
                .success(function (data) {
                    $window.localStorage.setItem('token', data.token);
                    $location.path('/');
                })
                .error(function (data) {
                    $scope.loginError = getErrorMessage(data, 'Login failed');
                    $('#loginButton').button('reset');
                });
        };
    }])

    .controller('CreateAccountCtrl', ['$scope', '$http', '$window', '$location', function ($scope, $http, $window, $location) {
        $scope.createAccount = function (user) {
            $('#accountButton').button('loading');
            delete $scope.createAccountError;
            user.password = CryptoJS.SHA256($scope.password).toString(CryptoJS.enc.Hex);
            $http.post(APIConfig.user, user)
                .success(function (data) {
                    $window.localStorage.setItem('token', data.token);
                    $location.path('/');
                })
                .error(function (data) {
                    $scope.createAccountError = getErrorMessage(data, 'Creating account failed');
                    $('#accountButton').button('reset');
                });
        };
    }])

    .controller('TasksCtrl', ['$scope', '$http', '$window', '$location', function ($scope, $http, $window, $location) {
        $scope.tasks = [];
        $http.get(APIConfig.tasks)
            .success(function (data) {
                if (angular.isArray(data)) {
                    angular.forEach(data, function (task) {
                        $scope.tasks.push(task);
                    });
                }
            })
            .error(function (data) {
                $scope.tasksError = getErrorMessage(data, 'Getting all tasks failed');
            });

        $scope.addTask = function () {
            $('#addButton').button('loading');
            var newTask = {
                description: $scope.description
            };
            delete $scope.tasksError;
            delete $scope.description;
            $http.post(APIConfig.tasks, newTask)
                .success(function (data) {
                    $scope.tasks.push(data);
                    $('#addButton').button('reset');
                })
                .error(function (data) {
                    $scope.tasksError = getErrorMessage(data, 'Adding new task failed');
                    $('#addButton').button('reset');
                });
        };

        $scope.markDone = function (task) {
            var updateTask = {
                description: task.description,
                completed: null == task.completionDate
            };
            delete $scope.tasksError;
            $http.put(APIConfig.tasks + '/' + task.id, updateTask)
                .success(function (data) {
                    task.completionDate = data.completionDate;
                })
                .error(function (data) {
                    $scope.tasksError = getErrorMessage(data, 'Marking task as done failed');
                });
        };

        $scope.delete = function (task, index) {
            delete $scope.tasksError;
            $http.delete(APIConfig.tasks + '/' + task.id)
                .success(function () {
                    $scope.tasks.splice(index, 1);
                })
                .error(function (data) {
                    $scope.tasksError = getErrorMessage(data, 'Deleting task failed');
                });
        };

        $scope.remaining = function () {
            var count = 0;
            angular.forEach($scope.tasks, function (task) {
                count += task.completionDate ? 0 : 1;
            });
            return count;
        };
    }]);

function getErrorMessage(response, messagePrefix) {
    var message = '';
    if (response && angular.isArray(response.errors)) {
        angular.forEach(response.errors, function (error) {
            message = message.concat(', ', error.message);
        });
    }
    if (messagePrefix) {
        message = messagePrefix.concat(message);
    }
    else if (message.indexOf(', ') == 0) {
        message = message.substring(2);
    }
    return message;
}

function logout() {
    window.localStorage.removeItem('token');
    window.location.href = '/';
}