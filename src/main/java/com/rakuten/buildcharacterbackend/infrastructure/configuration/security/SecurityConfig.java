package com.rakuten.buildcharacterbackend.infrastructure.configuration.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${auth.user.username}")
    private String username;
    @Value("${auth.user.password}")
    private String userpassword;
    @Value("${auth.admin.username}")
    private String adminname;
    @Value("${auth.admin.password}")
    private String adminpassword;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //@formatter:off
        http.cors().and().csrf().disable()
        .authorizeRequests()
        .antMatchers("/api/**").hasRole("USER")
            .and()
        .formLogin()
            .and()
        .httpBasic();
        //@formatter:off
    }

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedMethods("GET","PUT","POST","DELETE").allowedOrigins("*");
			}
		};
	}

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        //@formatter:off
        auth.inMemoryAuthentication()
        .withUser(username).password(passwordEncoder.encode(userpassword)).roles("USER")
        .and()
        .withUser(adminname).password(passwordEncoder.encode(adminpassword)).roles("USER","ADMIN");
        //@formatter:off
    }
}
