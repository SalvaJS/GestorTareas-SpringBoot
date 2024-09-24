package com.empresa.gestortareas.seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Autowired
    private UserDetailsServiceImplPropia userDetailsServiceImplPropia;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/", "/register").permitAll()
				.anyRequest().authenticated()
			)
			.formLogin((form) -> form
				.loginPage("/login")
				.defaultSuccessUrl("/menuUsuario", true)
				.permitAll()
			)
			.logout((logout) -> logout
					.logoutUrl("/logout")
					.logoutSuccessUrl("/")
					.permitAll())
			.authenticationProvider(authenticationProvider());
		return http.build();
	}

    @Bean
    UserDetailsService userDetailsService() {
		return userDetailsServiceImplPropia;
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationProvider authenticationProvider() {
	    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	    provider.setUserDetailsService(userDetailsService());
	    provider.setPasswordEncoder(passwordEncoder());
	    return provider;
	}
	
	@Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
