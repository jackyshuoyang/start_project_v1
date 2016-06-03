package com.demo;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
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

import com.demo.bean.Product;
import com.demo.dao.ProductDAO;

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
	@RequestMapping("/delete_product")
	public String deleteProduct(int id)
	{
		ProductDAO productDAO = appContext.getBean(ProductDAO.class);
		return productDAO.deleteProduct(id);
	}
	 
	@RequestMapping("/insert_product")
	public int addProduct(@RequestBody Product product){
		ProductDAO productDAO = appContext.getBean(ProductDAO.class);
		int returnId =  productDAO.save(product);
		return returnId;
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
					.antMatchers("/index.html", "/home.html", "/login.html","/add_product.html","/productlist.html", "/").permitAll().anyRequest()
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
