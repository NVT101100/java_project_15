package com.SocialNetwork;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.SocialNetwork.Service.MyUserDetailsService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	MyUserDetailsService userDetailsService;

	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		// Password encoder, để Spring Security sử dụng mã hóa mật khẩu người dùng
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(authProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.rememberMe().key("Secret").tokenValiditySeconds(3600);
		http.authorizeRequests()
				.antMatchers("/register","/login","/error","/invalid").permitAll()
				.antMatchers("/css/**","/js/**","/img/**","/fonts/**").permitAll()
				.antMatchers("/user/**","/").hasAnyAuthority("user","admin")
				.antMatchers("/admin/**").hasAuthority("admin")
				.anyRequest().authenticated()
				.and().formLogin().loginPage("/login").passwordParameter("password")
				.usernameParameter("email").defaultSuccessUrl("/user/index")
				.failureUrl("/login/error")
				.permitAll().and().logout().logoutSuccessUrl("/login")
				.permitAll();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web
        .ignoring()
        .antMatchers("/**/*.{js,css,jpg}");
	}
}