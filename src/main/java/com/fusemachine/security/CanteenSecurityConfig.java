package com.fusemachine.security;

import com.fusemachine.util.JwtAuthenticationEntryPoint;
import com.fusemachine.filter.JwtRequestFilter;
import com.fusemachine.service.FuseUserDetailsService;
import com.fusemachine.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class CanteenSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private FuseUserDetailsService userDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/login").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/foods/**").hasAnyRole("EMPLOYEE","ADMIN")
                    .antMatchers(HttpMethod.GET, "/api/menus/**").hasAnyRole("EMPLOYEE","ADMIN")
                    .antMatchers("/api/foods/**").hasRole("ADMIN")
                    .antMatchers("/api/menus/**").hasRole("ADMIN")
                    .antMatchers("/api/orders/**", "/api/requests/**").hasRole("ADMIN")
                    .antMatchers("/api/users", "/api/users/*", "/api/roles/**").hasRole("ADMIN")
                    .antMatchers("/api/users/*/orders/**").hasRole("EMPLOYEE")
                    .antMatchers("/api/users/*/requests").hasRole("EMPLOYEE")
                    .anyRequest().authenticated()
                    .and()
                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .and()
                        .httpBasic().authenticationEntryPoint(authenticationEntryPoint);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
}
