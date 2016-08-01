package com.demo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.sql.rowset.serial.SerialBlob;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.annotation.Order;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;

import com.demo.bean.Cost;
import com.demo.bean.DisplayShipment;
import com.demo.bean.Document;
import com.demo.bean.EventLog;
import com.demo.bean.EventLogWithFilenames;
import com.demo.bean.Job;
import com.demo.bean.JobCostMap;
import com.demo.bean.ProcurementOrder;
import com.demo.bean.Product;
import com.demo.bean.ProductOrderMapping;
import com.demo.bean.ProductsInOrder;
import com.demo.bean.Shipment;
import com.demo.bean.ShipmentJobMap;
import com.demo.dao.CostDAO;
import com.demo.dao.DocumentDAO;
import com.demo.dao.EventLogDAO;
import com.demo.dao.JobCostMapDAO;
import com.demo.dao.JobDAO;
import com.demo.dao.ProcurementOrderDAO;
import com.demo.dao.ProductDAO;
import com.demo.dao.ProductOrderMappingDAO;
import com.demo.dao.ShipmentDAO;
import com.demo.dao.ShipmentJobMapDAO;

@RestController
@SpringBootApplication
@ImportResource("spring.xml")
public class StartProjectV1Application {

	public StartProjectV1Application() {
		super();
		
	}
	
	private static final int BUFFER_SIZE = 4096;
	
	@Autowired
	private ApplicationContext appContext;

    
	public static void main(String[] args) {
		
		SpringApplication.run(StartProjectV1Application.class, args);
		
	}
	
	public int sumup(int a,int b){
		return a+b;
	}
	
	
	/**
	 * Upload multiple file using Spring Controller
	 * @throws Exception 
	 */
	@RequestMapping("/uploadFile")
	public int uploadMultipleFileHandler(
			@RequestParam MultipartFile file) throws Exception {
		Document doc;
		int returnId = -1;
		try{
			doc = new Document();
			Date currentTime = new Date();
			
			byte[] fileByte = file.getBytes();
			Blob blob = new SerialBlob(fileByte);
			doc.setFileName(currentTime.getTime() +"_"+file.getOriginalFilename());
			doc.setCreationTime(currentTime);
			doc.setContent(blob);
			doc.setContentType(file.getContentType());
			doc.setFileSize(file.getSize());
			DocumentDAO docDAO = appContext.getBean(DocumentDAO.class);
			returnId = docDAO.save(doc);
		}catch(Exception e){
			System.out.println("uploadMultipleFileHandler : " +e.toString());
			throw new Exception("uploadMultipleFileHandler : " +e.toString());
			
		}
		return returnId;
	}
	
	@RequestMapping("/insert_log_to_order")
	public int saveLogForOrder(@RequestBody EventLog log){
		EventLogDAO evtDAO = appContext.getBean(EventLogDAO.class);
		return evtDAO.save(log);
	}
	
	@RequestMapping("/remove_event")
	public void deleteEvent(@RequestBody int eventId){
		EventLogDAO evtDao = appContext.getBean(EventLogDAO.class);
		evtDao.delete(eventId);
	}
	
	@RequestMapping("/get_logs_for_order")
	public List<EventLogWithFilenames> getLogsForOrderByOrder(@RequestBody int orderId){
		EventLogDAO eventLogDAO = appContext.getBean(EventLogDAO.class);
		
		List<EventLog>eventList =  eventLogDAO.getOrderEventLog(orderId);
		List<EventLogWithFilenames> returnList = new ArrayList<EventLogWithFilenames>();
		for(EventLog e : eventList){
			EventLogWithFilenames eventWithFN = EventLogWithFilenames.construct(e);
			if(e.documentUrl!=null && (!e.documentUrl.equals(""))){
				eventWithFN.fileNames = Arrays.asList(constructFileNameArray(e.documentUrl));
			}
			returnList.add(eventWithFN);
		}
		return returnList;
	}
	
	
	/**
	 * @param docUrl
	 * @return
	 */
	private String[] constructFileNameArray(String docUrl){
		String [] idArray = docUrl.split(",");
		String [] returnArray=new String[idArray.length];
		DocumentDAO docDAO = appContext.getBean(DocumentDAO.class);
		for(int i=0;i<idArray.length;i++){
			returnArray[i] = docDAO.getFileNameById(Integer.parseInt(idArray[i]));
		}
		return returnArray;
	}
	
	
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/download" , method=RequestMethod.GET)
	public void downloadFile(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		String stId = request.getParameter("documentId");
		DocumentDAO docDao = appContext.getBean(DocumentDAO.class);
		Document d = docDao.getFileById(Integer.parseInt(stId));
		
		try {
			InputStream inputStream = d.getContent().getBinaryStream();
			
			response.setContentLength((int)d.getFileSize());
			response.setContentType(d.getContentType());
			// set headers for the response
			String nameValue = "attachment; filename=\""+d.getFileName()+"\"";
	        response.setHeader("Content-Disposition", nameValue);
	        
	        FileCopyUtils.copy(inputStream, response.getOutputStream());
	        
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	@RequestMapping("/shipments")
	public List<DisplayShipment>getShipmentList(){
		ShipmentDAO shipmentDAO = appContext.getBean(ShipmentDAO.class);
		List<Shipment> shipmentList = shipmentDAO.getShipmentList();
		List<DisplayShipment>returnList = new ArrayList<DisplayShipment>();
		DisplayShipment dShipment;
		for(Shipment shipment :shipmentList){
			dShipment = new DisplayShipment(shipment);
			dShipment.cost = getCostForShipment(dShipment.getId());
			returnList.add(dShipment);
		}
		return returnList;
	}
	
	private float getCostForShipment(int shipmentId){
		
		ShipmentJobMapDAO sjDAO = appContext.getBean(ShipmentJobMapDAO.class);
	    List<ShipmentJobMap> shipmentJobMapList = sjDAO.getJobShipmentMapByShipmentId(shipmentId);
	    float totalCost = 0;
	    for(ShipmentJobMap m:shipmentJobMapList){
	    	totalCost+=getCostForJob(m.getJob_id());
	    }
	    return totalCost;
	}
	
	private float getCostForJob(int jobId){
		JobCostMapDAO jcMapDAO = appContext.getBean(JobCostMapDAO.class);
		CostDAO costDAO = appContext.getBean(CostDAO.class);
		float totalCost=0;
		List<JobCostMap>costList = jcMapDAO.getCostForJob(jobId);
		for(JobCostMap c : costList){
			totalCost+=costDAO.getCostById(c.getCost_id()).getAmount();
		}
		return totalCost;
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
					.antMatchers("/index.html", "/home.html", "/login.html","/add_product.html","/productlist.html","/procurement_order.html", "/order_details.html","/create_order.html","/show_line.html","add_log_to_order.html","shipment_list.html","/").permitAll().anyRequest()
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
