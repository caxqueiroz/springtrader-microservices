package io.pivotal.springtrader.web.config;


import io.pivotal.springtrader.web.security.CustomAuthenticationProvider;
import io.pivotal.springtrader.web.security.CustomCredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private CustomCredentialsService customCredentialsService;
	
	@Autowired
	private CustomAuthenticationProvider customAuthenticationProvider;
	
	@Autowired
	private LogoutSuccessHandler logoutSuccessHandler;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/registration","/hystrix.stream").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .permitAll()
                .and()
            .logout()
            .logoutSuccessHandler(logoutSuccessHandler)
                .permitAll();
    }
	@Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/webjars/**")
                .antMatchers("/images/**").antMatchers("/css/**").antMatchers("/js/**");
    }
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(customAuthenticationProvider);
		auth.userDetailsService(customCredentialsService);

	}
}
