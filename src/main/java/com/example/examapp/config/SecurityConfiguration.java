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
	
	@Autowired
	private MyLogoutSuccessHandler myLogoutSuccessHandler;

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
				.antMatchers("/").fullyAuthenticated()
				.antMatchers("/adminregistration/*", "/roleregistration").permitAll()
				.antMatchers("/studentregistration/*").hasRole("STUDENT")
				.antMatchers("/admissionlist/*").hasRole("ADMIN")
				.antMatchers("/registeredstudents/*").hasAnyRole("ADMIN", "USER")
				.antMatchers("/cardverification/*").permitAll()
				.antMatchers("/changepassword/*").hasAnyRole("ADMIN", "USER")
				.antMatchers("/course/*", "/subject/*").hasRole("ADMIN")
				.antMatchers("/employee/*", "/employeeprofile/*").hasRole("ADMIN")
				.antMatchers("/settings/*", "/assignexamdate", "/togglesetting").hasRole("ADMIN")
				.antMatchers("/pingenerator/*").hasRole("VENDOR")
				.antMatchers("/profile/*").hasAnyRole("ADMIN","USER")
				.antMatchers("/question/*").hasRole("ADMIN")
				.antMatchers("/role/*").hasRole("ADMIN")
				.antMatchers("/endexam", "/examoption", "/getResult").hasRole("STUDENT")
				.antMatchers("/logout", "/login").permitAll()
				.antMatchers("/authenticate", "/examquestion/*", "/questionproperty/*").permitAll()
				.antMatchers("/student/*").hasRole("ADMIN")
				.antMatchers("/student/*").hasRole("ADMIN")
				.antMatchers("/studentprofile/*").hasRole("ADMIN")
				.antMatchers("/disablederrorpage/*").permitAll()
				.anyRequest().authenticated()
				.and()
				.csrf().ignoringAntMatchers("/authenticate","/questionproperty/*", "/examquestion/*")
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
					.logoutSuccessHandler(myLogoutSuccessHandler)
					.invalidateHttpSession(true)
					.deleteCookies("JSESSIONID")
					.permitAll()
					.and()
				.exceptionHandling()
					.accessDeniedPage("/access-denied")//"/403".
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