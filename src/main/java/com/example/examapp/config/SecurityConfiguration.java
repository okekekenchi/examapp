package com.example.examapp.config;

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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled= true, jsr250Enabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	UserDetailsService userDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.userDetailsService(userDetailsService)
			.passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {

		http.			
			authorizeRequests()
				.antMatchers("/index", "/settings", "/disablederrorpage", "/assignexamdate").permitAll()
				.antMatchers("/questionproperty/*", "/registeredstudents").permitAll()
				.antMatchers("/examquestion/*").permitAll()
				.antMatchers("/cardverification").permitAll()
				.antMatchers("/logout").permitAll()
				.antMatchers("/login","/authenticate").permitAll()
				.antMatchers("/adminregistration", "/roleregistration", "/employee").permitAll()
				.antMatchers("/pingenerator").permitAll()
				.antMatchers("/subject", "/subject/*", "/question", "/question/*", "/course", "/course/*").permitAll()
				.antMatchers("/student/*","/student", "/admissionlist", "/admissionlist/*").permitAll()
				.antMatchers( "/employee/*","/role", "/role/*").permitAll()
				.anyRequest().authenticated()
				.and()
				.formLogin()
					.loginPage("/login")
					.failureUrl("/login?error=true")
					.loginProcessingUrl("/login")
					.successHandler(myAuthenticationSuccessHandler())
					.usernameParameter("username")
					.passwordParameter("password")
					.and()
				.logout()
					.logoutSuccessUrl("/login")
					.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
					.invalidateHttpSession(true)
					.deleteCookies("JSESSIONID")
					.permitAll()
					.and()
				.exceptionHandling()
					.accessDeniedPage("/access-denied")//"/403"
					.and()
				.sessionManagement()
					.maximumSessions(1)				
                .expiredUrl("/login");
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web
	       .ignoring()	       
	       .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
	    web.ignoring().antMatchers("/css/**");
        web.ignoring().antMatchers("/scripts/**");
        web.ignoring().antMatchers("/images/**");
        web.ignoring().antMatchers("/assets/**");
	}
	
	@Bean
	public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
		return new MySimpleUrlAuthenticationSuccessHandler();
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception{
		 return super.authenticationManagerBean();
	}
}