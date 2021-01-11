package com.advinow.mica;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
/*
import com.advinow.mica.util.gzip.GZIP2WayResponseStream;
import com.advinow.mica.util.gzip.GZIP2WayResponseWrapper;*/

/**
 * 
 * 
 * 
 * 
 * 
 * @author Govinda Reddy
 *
 */
@Component
public class MICACORSFilter implements Filter {
    /**
     * 
     */
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;
			response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
			
		
		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			chain.doFilter(req, res);
		}
	}


	@Override
	public void init(FilterConfig filterConfig) {}

	@Override
	public void destroy() {
		
	}
	
}


