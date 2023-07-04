package com.tuespotsolutions.security.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tuespotsolutions.security.service.UserDetailsServiceImpl;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		// securedEnabled = true,
		// jsr250Enabled = true,
		prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	private static final String[] AUTH_BYPASS_ROUTES = { "/hello/**", "/otp/**", "/roles/add", "/state/**",
			"/district/**", "/city/**", "/candidate/registeration", "/candidate/enterotp", "/company/register",
			"/company/enterotp", "/job/search","/job/filters","/job/findJobWithFilters", "/job/with/**",
			"/paymentgateway/update/payment/status/**", "/job/all/cities/list", "/filter/get/enabled/filters",
			"/filter/get/location/filter/by/**", "/filter/get/job/with/locationfilter/by/**","/packages/get/company/for/website",
			"/packages/get/candidate/for/website","/filter/get/postedon/filters", "/filter/get/postedon/job/list/**",
			"/filter/get/workmode/filters", "/filter/get/by/workmode/job/list","/filter/filterlist/with/values",
			"/filter/filterlist/with/jobcount/by/**","/filter/list/by/checked/filters/**","/home/page/job","/home/pills",
			"/home/company/logo"

	};

	private static final String[] AUTH_WHITELIST_JWT = { "/auth/signin", "/swagger-ui/**" };

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
				.antMatchers(AUTH_WHITELIST_JWT).permitAll().antMatchers(AUTH_BYPASS_ROUTES).permitAll().anyRequest()
				.authenticated();

		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/**",
				"/swagger-ui.html", "/webjars/**");

	}

}
