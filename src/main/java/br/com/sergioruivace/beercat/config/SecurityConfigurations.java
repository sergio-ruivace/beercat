package br.com.sergioruivace.beercat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;	
	
	@Autowired
	private AuthTokenFilter authTokenFilter;
	
	@Value("${beercat.devMode}")
	private Boolean isDevMode;

	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//Development Environment Only
		if(isDevMode) {
			http.authorizeRequests()
				.antMatchers("/h2-console").permitAll()
				.antMatchers("/h2-console/**").permitAll();
		}
		
		http.authorizeRequests()
			.antMatchers(HttpMethod.GET, "/beers").permitAll()
			.antMatchers(HttpMethod.GET, "/beers/**").permitAll()
			.antMatchers(HttpMethod.GET, "/beerPictures").permitAll()
			.antMatchers(HttpMethod.GET, "/beerPictures/**").permitAll()
			.antMatchers(HttpMethod.GET, "/manufacturers").permitAll()
			.antMatchers(HttpMethod.GET, "/manufacturers/**").permitAll()
			.antMatchers(HttpMethod.POST, "/auth").permitAll()			
			.anyRequest().authenticated()
			.and().csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and().addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
	
	@Override
	public void configure(WebSecurity web) throws Exception {

		if (isDevMode) {
			web.ignoring().antMatchers("/h2-console/**", "/swagger-ui/**", "/v3/api-docs/**");			
		}
		else {
			web.ignoring().antMatchers("/swagger-ui/**", "/v3/api-docs/**");
		}

	}
}
