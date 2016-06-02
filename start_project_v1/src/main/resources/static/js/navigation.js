angular.module('hello', [ 'ngRoute' ]).config(function($routeProvider, $httpProvider) {

	$routeProvider.when('/', {
		templateUrl : 'home.html',
		controller : 'home',
		controllerAs: 'controller'
	}).when('/login', {
		templateUrl : 'login.html',
		controller : 'navigation',
		controllerAs: 'controller'
	}).when('/add_product',{
		templateUrl: 'add_product.html',
		controller: 'addProduct',
		controllerAs:'controller'
	}).otherwise('/');

	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

}).controller('navigation',

		function($rootScope, $http, $location, $route) {
			
			var self = this;

			self.tab = function(route) {
				return $route.current && route === $route.current.controller;
			};

			var authenticate = function(credentials, callback) {

				var headers = credentials ? {
					authorization : "Basic "
							+ btoa(credentials.username + ":"
									+ credentials.password)
				} : {};

				$http.get('user', {
					headers : headers
				}).then(function(response) {
					if (response.data.name) {
						$rootScope.authenticated = true;
					} else {
						$rootScope.authenticated = false;
					}
					callback && callback($rootScope.authenticated);
				}, function() {
					$rootScope.authenticated = false;
					callback && callback(false);
				});

			}

			authenticate();

			self.credentials = {};
			self.login = function() {
				authenticate(self.credentials, function(authenticated) {
					if (authenticated) {
						console.log("Login succeeded")
						$location.path("/");
						self.error = false;
						$rootScope.authenticated = true;
					} else {
						console.log("Login failed")
						$location.path("/login");
						self.error = true;
						$rootScope.authenticated = false;
					}
				})
			};

			self.logout = function() {
				$http.post('logout', {}).finally(function() {
					$rootScope.authenticated = false;
					$location.path("/");
				});
			}

		})

.controller('home', function($http) {
	var self = this;
	
	$http.get('/resource/').then(function(response) {
		self.greeting = response.data;
	})
})
.controller('addProduct',function($http,$log,$scope){
	self = this;
	self.newProduct={};
	
	self.addNewProduct = function(){
		
		
		var res = $http.post('/insert_product',self.newProduct);
		$log.log("saved error: " + self.newProduct.error);
		res.success(function(data,status,headers,config){
			//adding to local list.
			self.newProduct.id = data;
			self.newProduct.error = false;
			$log.log("self.newProduct.name:"+self.newProduct.name);
			
			alert(self.newProduct.name+" has been added, id is "+data+".");
			self.newProduct={};
		});
		
		res.error(function(data,status,headers,config){
			self.newProduct.error = true;
			alert("Adding new product failed:"+data);
		});
		
		
	};
});