package com.tuespotsolutions.security.configuration;

import java.io.IOException;
import org.springframework.util.StringUtils;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tuespotsolutions.security.service.UserDetailsServiceImpl;

public class AuthTokenFilter extends OncePerRequestFilter {
	
	Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			
			
			String jwt = parseJwt(request);
			if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
				String username = jwtUtils.getUserNameFromJwtToken(jwt);
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
				logger.info("line no 47 doFilterInternal() method");
			}
		} catch (Exception e) {
			
			logger.error("line no 50 doFilterInternal() method, message :"+e.getMessage());
			e.printStackTrace();
		}

		filterChain.doFilter(request, response);

	}

	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");

		logger.info("line no 62 doFilterInternal() method");
		
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			
			logger.info("line no 66 doFilterInternal() method");
			
			return headerAuth.substring(7, headerAuth.length());
		}

		return null;
	}
}
