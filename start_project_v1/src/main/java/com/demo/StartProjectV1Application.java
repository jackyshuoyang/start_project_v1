package com.demo;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import com.demo.bean.EventLog;
import com.demo.bean.ProcurementOrder;
import com.demo.bean.Product;
import com.demo.bean.ProductOrderMapping;
import com.demo.bean.ProductsInOrder;
import com.demo.dao.EventLogDAO;
import com.demo.dao.ProcurementOrderDAO;
import com.demo.dao.ProductDAO;
import com.demo.dao.ProductOrderMappingDAO;

@RestController
@SpringBootApplication
@ImportResource("spring.xml")
public class StartProjectV1Application {

	public StartProjectV1Application() {
		super();
		
	}
	
	@Autowired
	private ApplicationContext appContext;

    
	public static void main(String[] args) {
		
		SpringApplication.run(StartProjectV1Application.class, args);
		
	}
	
	@RequestMapping("/insert_log_to_order")
	public int saveLogForOrder(@RequestBody EventLog log){
		EventLogDAO evtDAO = appContext.getBean(EventLogDAO.class);
		return evtDAO.save(log);
	}
	
	@RequestMapping("/get_logs_for_order")
	public List<EventLog>getLogsForOrderByOrder(@RequestBody int orderId){
		EventLogDAO eventLogDAO = appContext.getBean(EventLogDAO.class);
		return eventLogDAO.getOrderEventLog(orderId);
		
	}
	
	@RequestMapping("/get_products_for_order")
	public List<ProductsInOrder> getProductsByOrderId(@RequestBody int orderId)
	{
		//initialization 
		ProductOrderMappingDAO productOrderMapping = appContext.getBean(ProductOrderMappingDAO.class);
		ProductDAO productDAO = appContext.getBean(ProductDAO.class);
		
		//get product list based on order id from mapping table
		List<ProductOrderMapping> mapping = productOrderMapping.getProductMappingForOrder(orderId);
		//get product details based on product id;
		ArrayList<ProductsInOrder> returnList = new ArrayList<ProductsInOrder>();
		for(int i=0;i<mapping.size();i++)
		{
			int productId = mapping.get(i).productId;
			Product returnP = productDAO.getProductById(productId);
			ProductsInOrder p = new ProductsInOrder();
			p.setFob(returnP.getFob());
			p.setId(returnP.getId());
			p.setImageUrl(returnP.getImageUrl());
			p.setName(returnP.getName());
			p.setQty(mapping.get(i).qty);
			p.setOrderId(orderId);
			returnList.add(p);
		}
		return returnList;
	}
	
	@RequestMapping("/updateProductQtyForOrder")
	public void updateProductQtyForOrder(@RequestBody ProductOrderMapping mapping){
		//initialization 
		ProductOrderMappingDAO productOrderMapping = appContext.getBean(ProductOrderMappingDAO.class);
		productOrderMapping.updateMapping(mapping);	
		
	}
	
	@RequestMapping("/resource")
	public Map<String,Object> home() {
	    Map<String,Object> model = new HashMap<String,Object>();
	    model.put("id", UUID.randomUUID().toString());
	    model.put("content", "Server provided data!!");
	    return model;
	}
	@RequestMapping("/products")
	public List<Product> getProductList(){
		ProductDAO productDAO = appContext.getBean(ProductDAO.class);
		List<Product>list = productDAO.list();
		return list;
	}
	/**
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete_product")
	public int deleteProduct(@RequestBody Product p)
	{
		ProductDAO productDAO = appContext.getBean(ProductDAO.class);
		int rowAffected = productDAO.deleteProduct(p.getId());
		return rowAffected;
	}
	
	@RequestMapping("/insert_order")
	public void addOrder(@RequestBody ProcurementOrder order)
	{
		ProcurementOrderDAO orderDAO= appContext.getBean(ProcurementOrderDAO.class);
		order.setCreationDate(new Date());
		orderDAO.save(order);
	}
	 
	@RequestMapping("/insert_product")
	public int addProduct(@RequestBody Product product){
		ProductDAO productDAO = appContext.getBean(ProductDAO.class);
		int returnId =  productDAO.save(product);
		return returnId;
	}
	
	@RequestMapping("/orders")
	public List<ProcurementOrder> getProcurementOrders(){
		ProcurementOrderDAO procurementOrderDAO = appContext.getBean(ProcurementOrderDAO.class);
		return procurementOrderDAO.list();
	}
	
	@RequestMapping("/delete_procurement_order")
	public int deleteProcurementOrder(@RequestBody ProcurementOrder p){
		ProcurementOrderDAO orderDAO = appContext.getBean(ProcurementOrderDAO.class);
		int rowAffected = orderDAO.deleteProcurementOrder(p.getId());
		ProductOrderMappingDAO mapDAO = appContext.getBean(ProductOrderMappingDAO.class);
		//delete all mappings
		mapDAO.deleteMappingBasedOnOrderId(p.getId());
		
		return rowAffected;
	}
	
	@RequestMapping("/update_order")
	public int updateOrSaveOrderDetails(@RequestBody ProcurementOrder p){
		ProcurementOrderDAO pDAO = appContext.getBean(ProcurementOrderDAO.class);
		int returnId =  pDAO.updateProcurementOrder(p);
		return returnId;
	}
	
	@RequestMapping("/remove_product_from_order")
	public void deleteProductFromOrder(@RequestBody ProductsInOrder pInOrder){
		ProductOrderMappingDAO pDAO = appContext.getBean(ProductOrderMappingDAO.class);
		ProcurementOrderDAO procurementOrderDAO = appContext.getBean(ProcurementOrderDAO.class);
		pDAO.deleteProductFromOrder(pInOrder.getId(), pInOrder.getOrderId());
	}
	
	@RequestMapping("/insert_product_to_order")
	public void addProductToOrder(@RequestBody ProductOrderMapping mapping){
	
		ProductOrderMappingDAO pDAO = appContext.getBean(ProductOrderMappingDAO.class);
		pDAO.saveProductAndOrderMaps(mapping);
	}
	
	
	 
	@RequestMapping("/user")
	public Principal user(Principal user) {
		System.out.println("xxxxxxxx Username : " + user.getName());
		
		System.out.println("xxxxxxxx toString : " + user.toString());
		return user;
	}
	
	@Configuration
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.httpBasic().and().authorizeRequests()
					.antMatchers("/index.html", "/home.html", "/login.html","/add_product.html","/productlist.html","/procurement_order.html", "/order_details.html","/create_order.html","/show_line.html","add_log_to_order.html","/").permitAll().anyRequest()
					.authenticated().and().csrf()
					.csrfTokenRepository(csrfTokenRepository()).and()
					.addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);
		}

		private Filter csrfHeaderFilter() {
			return new OncePerRequestFilter() {
				@Override
				protected void doFilterInternal(HttpServletRequest request,
						HttpServletResponse response, FilterChain filterChain)
						throws ServletException, IOException {
					CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class
							.getName());
					if (csrf != null) {
						Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
						String token = csrf.getToken();
						if (cookie == null || token != null
								&& !token.equals(cookie.getValue())) {
							cookie = new Cookie("XSRF-TOKEN", token);
							cookie.setPath("/");
							response.addCookie(cookie);
						}
					}
					filterChain.doFilter(request, response);
				}
			};
		}

		private CsrfTokenRepository csrfTokenRepository() {
			HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
			repository.setHeaderName("X-XSRF-TOKEN");
			return repository;
		}
	
	}
}
