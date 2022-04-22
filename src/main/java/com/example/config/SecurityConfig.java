package com.example.config;

import javax.persistence.Basic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}
	
	@Autowired
	UserDetailsServiceIml userDetailsServiceIml;
	
	
	
	 @Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsServiceIml).passwordEncoder(bCryptPasswordEncoder());
		
	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsServiceIml);
		authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
		return authenticationProvider;
	}
	 
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		
		
		http
        .authorizeRequests()
        .antMatchers("/", "/shop","/blog","/blog-details/**","/contact","/product-details/**","/sign-up","/user/confirm-email").permitAll()       
        .and()
        .authorizeRequests().antMatchers("/account").hasAnyRole("USER","ADMIN")
        .and()
        .authorizeHttpRequests().antMatchers("/admin","/list-product","/view-product/*").hasRole("ADMIN")             
        .and()
        .formLogin()
        .loginPage("/login")
        .defaultSuccessUrl("/")
        .permitAll() 
        .and()
        .logout() 
        .permitAll();
		
		
		
			
		
	}
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**", "/static/**","/css/**", "/js/**","/img/**","/fonts/**","/sass/**","/assets/**","/vendor/**","/ckeditor/**");
	}
}
