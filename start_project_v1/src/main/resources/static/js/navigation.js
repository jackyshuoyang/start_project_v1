
angular.module('hello', [ 'ngRoute','smart-table' ]).config(function($routeProvider, $httpProvider) {

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
	}).when('/products',{
		templateUrl: 'productlist.html',
		controller: 'productlistCtrl',
		controllerAs:'controller'
	}).when('/orders',{
		templateUrl: 'procurement_order_list.html',
		controller: 'orderCtrl',
		controllerAs:'controller'
	}).when('/view_procurement_order',{
		templateUrl: 'order_details.html',
		controller: 'procurementOrderCtrl',
		controllerAs:'controller'
	}).when('/create_new_order',{
		templateUrl: 'create_order.html',
		controller: 'addOrderCtrl',
		controllerAs:'controller'
	}).when('/update_product_qty_for_order',{
		templateUrl: 'update_qty_for_products_in_order.html',
		controller: 'updateQtyCtrl',
		controllerAs:'controller'
	}).when('/add_product_to_order',{
		templateUrl: 'add_product_to_order.html',
		controller: 'addProductToOrderCtrl',
		controllerAs:'controller'
	}).when('/add_log_to_order',{
		templateUrl: 'add_log_to_order.html',
		controller: 'addLogToOrderCtrl',
		controllerAs:'controller'
	}).otherwise('/');

	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
}).service('addProductToOrderService',function(){
	var productMapList={};
	return productMapList;
}).service('updateProductQtyForOrderService',function(){
	var selectedProductOrderMap={};
	return selectedProductOrderMap;
}).service('procurementOrderShareService',function(){
	//this service is for sharing selected procurementOrder between procurementOrderList and procurementOrder page.
	var selectedOrder={};
	return selectedOrder;
}).directive( "mwConfirmClick", [
    function( ) {
    	return {
    		priority: -1,
    		restrict: 'A',
    		scope: { confirmFunction: "&mwConfirmClick" },
    		link: function( scope, element, attrs ){
    			element.bind( 'click', function( e ){
    				// message defaults to "Are you sure?"
    				var message = attrs.mwConfirmClickMessage ? attrs.mwConfirmClickMessage : "Are you sure?";
    				// confirm() requires jQuery
    				if( confirm( message ) ) {
    					scope.confirmFunction();
    				}
    			});
    			}
    	}
    }
])
.controller('navigation',

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
						$location.path("/");
						self.error = false;
						$rootScope.authenticated = true;
					} else {
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
	});
})
.controller('productlistCtrl',function($http){
	var self = this;
	self.rowCollection=[];
	$http.get('/products/').then(function(response){
		self.rowCollection=[];
		var p;
		for(p in response.data){
			self.rowCollection.push(response.data[p]);
		}
		
	});
	
	self.removeItem = function(row){
		var res = $http.post('/delete_product',row);
		res.success(function(data,status,headers,config){
			if(data>0){
				var index = self.rowCollection.indexOf(row);
				if(index!==-1){
					self.rowCollection.splice(index,1);
					self.success=true;
					self.actionMsg = "product "+"' "+row.name+" '"+" has been deleted."
				}
			}else{
				self.error = true;
				self.actionMsg = "No product has been deleted.";
			}
				
		});
		res.error(function(data,status,headers,config){
			self.error = true;
			self.actionMsg = "There is something wrong with deleting this product.[Details: "+data+"]";
		});
	}
	
}).controller('orderCtrl',function($http,$window,$rootScope,procurementOrderShareService){
	
	//define status enumerator.
	var ORDER_STATUS = {
		PRE_MATURE:{value:0,name:"pre-mature"},
		IN_PROGRESS:{value:1,name:"in-progress"},
		COMPLETE:{value:2,name:"complete"}
	};
	
	var self = this;
	self.rowCollection=[];
	$http.get('/orders/').then(function(response){
		self.rowCollection=[];
		var p;
		for(p in response.data){
			var obj = response.data[p];
			if(obj.status == ORDER_STATUS.IN_PROGRESS.value){
				obj.daysDiff = Date.daysBetween(new Date(obj.startDate),new Date());
			}else{
				obj.daysDiff = 0;
			}
			self.rowCollection.push(obj);
		}
	});
	
	self.viewProcurementOrder = function(row){
		procurementOrderShareService.selectedOrder = row;
		$window.location.href = '#/view_procurement_order';
	};
	
	self.removeItem = function(row){
		var res = $http.post('/delete_procurement_order',row);
		res.success(function(data,status,headers,config){
			if(data>0){
				var index = self.rowCollection.indexOf(row);
				if(index!==-1){
					self.rowCollection.splice(index,1);
					self.success=true;
					self.actionMsg = "order "+"' "+row.summary+" '"+" has been deleted."
				}
			}else{
				self.error = true;
				self.actionMsg = "No order has been deleted.";
			}
				
		});
		res.error(function(data,status,headers,config){
			self.error = true;
			self.actionMsg = "There is something wrong with deleting this orders.[Details: "+data+"]";
		});
	};
	
	
}).controller('procurementOrderCtrl',function(procurementOrderShareService,addProductToOrderService,updateProductQtyForOrderService,$http,$scope,$window){
	
	self = this;
	
	self.order=procurementOrderShareService.selectedOrder;
	var orderMemoto = clone(self.order);
	var productCollection=[];
	var eventList=[];
	console.log("start to load logs");
	var res = $http.post("/get_products_for_order",self.order.id);
	res.success(function(data,status,headers,config){
		self.productCollection = data;
		
		addProductToOrderService.productMapList = self.productCollection;
		console.log("addProductToOrderService:");
		console.log(addProductToOrderService.productMapList);
		var totalFob = calculateOrderFobFromProductSum();
		
		if(self.order.fob!==totalFob){
			self.order.fob = totalFob;
			orderMemoto.fob = totalFob;
			updateProcurementOrder();
		}
	});
	
	var res2 = $http.post("/get_logs_for_order",self.order.id);
	res2.success(function(data,status,headers,config){
		self.eventList = data;
		console.log("eventList");
		console.log(self.eventList);
		
	});
	
	self.removeEvent=function(event){
		//todo:
	};
	
	self.viewEvent=function(event){
		//todo:
	};
	
	self.revertValue = function(){
		self.order = orderMemoto;
		orderMemoto = clone(orderMemoto);
	};
	
	self.updateOrderDetails=function(){
		updateProcurementOrder();
		self.success = true;
		self.actionMsg = "Order details updated.";
	};
	
	function updateProcurementOrder(){
		var updateBean = clone(self.order);
		var res = $http.post("/update_order",updateBean);
		res.success(function(data,status,headers,config){
			
			self.success=true;
			orderMemoto = clone(updateBean);
			self.actionMsg = "Procurement order has been updated."
		});
		
		res.error(function(data,status,headers,config){
			self.error = true;
			self.actionMsg = "There is something wrong with updating this orders.[Details: "+data+"]";
		});
	};
	
	function calculateOrderFobFromProductSum(){
		var totalFob = 0;
		for(index in self.productCollection){
			var singleFob = self.productCollection[index].fob;
			var qty = self.productCollection[index].qty;
			totalFob += singleFob * qty;
		}
		return totalFob;
	}
	
	function recalculateOrderFobAndUpdate(){
		var totalFob =  calculateOrderFobFromProductSum();
		orderMemoto.fob = totalFob;
		self.order.fob = totalFob;
	}
	
	self.updateQty = function(productRow){
		updateProductQtyForOrderService.orderId = self.order.id;
		updateProductQtyForOrderService.productId = productRow.id;
		updateProductQtyForOrderService.orderSummary = self.order.summary;
		updateProductQtyForOrderService.productName = productRow.name;
		updateProductQtyForOrderService.qty = productRow.qty;
		updateProductQtyForOrderService.selectedOrder = self.order;
		
		$window.location.href = '#/update_product_qty_for_order';
	}
	
	self.removeProductFromOrder=function(productRow){
		/*remove products from order will trigger 
		 * 1, delete mapping reference from mapping table
		 * 2, recalculate fob for an order on server side.
		 * 3, recalculate fob for an order on the client side.
		 * ***************************************************/
		var res = $http.post("/remove_product_from_order",productRow);
		res.success(function(data,status,headers,config){
			
			//delete productMapRow from productCollection;
			self.productCollection.splice(self.productCollection.indexOf(productRow),1);
			//update self.order and momento.
			recalculateOrderFobAndUpdate();
			
			self.success=true;
			self.error = false;
			self.actionMsg = "product has been removed from order."
		});
		
		res.error(function(data,status,headers,config){
			self.error = true;
			self.success = false;
			self.actionMsg = "There is something wrong with updating this orders.[Details: "+data+"]";
		});
	};
	
}).controller('addLogToOrderCtrl',function($http,$window,procurementOrderShareService){
	self = this;
	self.newEvent={};
	self.newEvent.dateOfLogging = new Date();
	self.newEvent.type = 1;
	self.newEvent.levelOfEmergencyBoolean = false;
	self.newEvent.levelOfEmergency = 1;
	self.newEvent.valid = true;
	self.newEvent.referralId = procurementOrderShareService.selectedOrder.id;
	self.selectedOrder = procurementOrderShareService.selectedOrder;
	console.log("addLogToOrderCtrl");
	console.log(self.newEvent);
	self.addLogToOrder = function(){
		if(self.newEvent.levelOfEmergencyBoolean) self.newEvent.levelOfEmergency=2;
		var res = $http.post('/insert_log_to_order',self.newEvent);
		res.success(function(data,status,headers,config){
			if(self.newEvent.actionType==2){
				self.selectedOrder.startDate = self.newEvent.dateOfEvent;
				self.selectedOrder.status = 1;//in progress.
			}
			procurementOrderShareService.selectedOrder = self.selectedOrder;
			$window.location.href = '#/view_procurement_order';
		});
		
		res.error(function(data,status,headers,config){
			self.error = true;
			self.actionMsg = "There is something wrong with adding event log!";
		});
	};
	
	
	
}).controller('addProductToOrderCtrl',function($http,$window,addProductToOrderService,procurementOrderShareService){
	self = this;
	self.selectedOrder = procurementOrderShareService.selectedOrder;
	self.productMapList = addProductToOrderService.productMapList;
	console.log("addProductTOOrderCTRL : ");
	console.log(addProductToOrderService.productMapList);
	
	self.qty=0;
	loadAllProducts();
	
	function loadAllProducts(){
		self.productCollection=[];
		$http.get('/products/').then(function(response){
			self.productCollection=[];
			var p;
			for(p in response.data){
				if(inTheList(response.data[p].id)==false){
					self.productCollection.push(response.data[p]);
				}
			}
		});
	}
	
	function inTheList(productId){
		if(self.productMapList==null)return false;
		for(p in self.productMapList){
			if(self.productMapList[p].id ==productId)
				return true;
		}
		return false;
	}
	
	
	self.addProductAndOrderMapping = function(){
		var productSelected = self.productCollection.filter(function(item){return item.isSelected===true;});
		
		
		if(productSelected[0]==null){
			self.error=true;
			self.actionMsg = "Please select at least one product.";
		}else if(self.qty==0){
			self.error=true;
			self.actionMsg = "Quantity must be greater than 0.";
		}else{
			var sendingMap={};
			sendingMap.id=0;
			console.log(" productSelected.id :");
			console.log(productSelected[0]);
			sendingMap.productId = productSelected[0].id;
			sendingMap.orderId = self.selectedOrder.id;
			sendingMap.qty = self.qty;
			console.log(" sendingMap :");
			console.log(sendingMap);
			var res = $http.post("/insert_product_to_order",sendingMap);
			res.success(function(data,status,headers,config){
				procurementOrderShareService.selectedOrder = self.selectedOrder;
				$window.location.href = '#/view_procurement_order';
			});
			
			res.error(function(data,status,headers,config){
				self.error = true;
				self.actionMsg = "There is something wrong with updating this orders.[Details: "+data+"]";
			});
			
		}
	};
	
	
	
}).controller('updateQtyCtrl',function($http,$window,updateProductQtyForOrderService,procurementOrderShareService){
	self = this;
	self.orderId = updateProductQtyForOrderService.orderId;
	self.productId = updateProductQtyForOrderService.productId;
	self.summary = updateProductQtyForOrderService.orderSummary;
	self.productName = updateProductQtyForOrderService.productName;	
	self.qty = updateProductQtyForOrderService.qty;
	self.qtyMomento = updateProductQtyForOrderService.qty;
	self.selectedOrder = updateProductQtyForOrderService.selectedOrder;
	
	self.updateQuantity = function(){
		if(self.qty!=self.qtyMomento){
			var productOrderMapping = {};
			productOrderMapping.id=0;
			productOrderMapping.orderId = self.orderId;
			productOrderMapping.productId = self.productId;
			productOrderMapping.qty = self.qty;
			console.log("fire  ->self:: orderid"+self.orderId);
			console.log("fire ->self:: productId"+self.productId);
			console.log("fire ->self:: qty"+self.qty);
			console.log("fire  ->productOrderMapping:: orderid "+productOrderMapping.orderId);
			console.log("fire ->productOrderMapping:: pid "+productOrderMapping.productId);
			console.log("fire ->productOrderMapping:: qty "+productOrderMapping.qty);
			var res = $http.post("/updateProductQtyForOrder",productOrderMapping);
			res.success(function(data,status,headers,config){
				procurementOrderShareService.selectedOrder = self.selectedOrder;
				$window.location.href = '#/view_procurement_order';
			});
			
			res.error(function(data,status,headers,config){
				self.error = true;
				self.actionMsg = "There is something wrong with updating this orders.[Details: "+data+"]";
			});
		}
		
	}
	
}).controller('addOrderCtrl',function($http,$window){
	self = this;
	self.newOrder = {};
	self.newOrder.creationDate = new Date();
	self.newOrder.status = 0;
	self.newOrder.startDate = null;
	self.newOrder.endDate = null;
	self.newOrder.fob = 0;
	self.newOrder.summary = "";
	self.newOrder.manufactoryOrderId = "";
	self.newOrder.ManufactoryName="";
	
	self.addNewOrder = function(){
		var res = $http.post('/insert_order',self.newOrder);
		res.success(function(data,status,headers,config){
			$window.location.href = '#/orders';
		});
		res.error(function(data,status,headers,config){
			self.error = true;
			self.errorMsg = "There is something wrong with initializing new procurement order."
		});
		
	}
	
	
}).controller('addProduct',function($http,$scope){
	self = this;
	self.newProduct={};
	
	self.addNewProduct = function(){
		
		
		var res = $http.post('/insert_product',self.newProduct);
		res.success(function(data,status,headers,config){
			//adding to local list.
			self.newProduct.id = data;
			self.newProduct.success = true;
			
			alert(self.newProduct.name+" has been added, id is "+data+".");
			self.newProduct={};
		});
		
		res.error(function(data,status,headers,config){
			self.newProduct.error = true;
			alert("Adding new product failed:"+data);
		});
		
		
	};
});






/**util function define**/
Date.daysBetween = function( date1, date2 ) {
	  //Get 1 day in milliseconds
	  var one_day=1000*60*60*24;

	  // Convert both dates to milliseconds
	  var date1_ms = date1.getTime();
	  var date2_ms = date2.getTime();

	  // Calculate the difference in milliseconds
	  var difference_ms = date2_ms - date1_ms;
	    
	  // Convert back to days and return
	  return Math.round(difference_ms/one_day); 
}


function clone(obj) {
    var copy;

    // Handle the 3 simple types, and null or undefined
    if (null == obj || "object" != typeof obj) return obj;

    // Handle Date
    if (obj instanceof Date) {
        copy = new Date();
        copy.setTime(obj.getTime());
        return copy;
    }

    // Handle Array
    if (obj instanceof Array) {
        copy = [];
        for (var i = 0, len = obj.length; i < len; i++) {
            copy[i] = clone(obj[i]);
        }
        return copy;
    }

    // Handle Object
    if (obj instanceof Object) {
        copy = {};
        for (var attr in obj) {
            if (obj.hasOwnProperty(attr)) copy[attr] = clone(obj[attr]);
        }
        return copy;
    }

    throw new Error("Unable to copy obj! Its type isn't supported.");
}