package com.erp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Bean
	public UserDetailsService users() {
		UserDetails user = User.builder()
			.username("GoutamBarfa")
			.password(passwordEncoder.encode("12345"))
			.roles("USER")
			.build();
		return new InMemoryUserDetailsManager(user);
	}
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
	    AuthenticationManagerBuilder authenticationManagerBuilder =
	        http.getSharedObject(AuthenticationManagerBuilder.class);
	    authenticationManagerBuilder
	        .userDetailsService(users())  
	        .passwordEncoder(passwordEncoder);
	    return authenticationManagerBuilder.build();
	}

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	  http
    	  .csrf(csrf -> csrf.disable() )
          .authorizeHttpRequests(authz -> authz
              .requestMatchers("/auth/login/**").permitAll()
              .anyRequest().authenticated()
          )
          .formLogin(form -> form.disable()
          );
      
      return http.build();
    }
}
