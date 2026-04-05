package com.edutech.educationalresourcedistributionsystem.config;

import com.edutech.educationalresourcedistributionsystem.jwt.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().and()

            .authorizeRequests()

            // ✅ PUBLIC APIs (NO JWT)
            .antMatchers(
                    "/api/user/register",
                    "/api/user/login",
                    "/api/user/forgot-password",
                    "/api/user/verify-otp",
                    "/api/user/reset-password"
            ).permitAll()

            // INSTITUTION APIs
            .antMatchers(HttpMethod.POST, "/api/institution/event").hasAuthority("INSTITUTION")
            .antMatchers(HttpMethod.GET, "/api/institution/events").hasAuthority("INSTITUTION")
            .antMatchers(HttpMethod.POST, "/api/institution/resource").hasAuthority("INSTITUTION")
            .antMatchers(HttpMethod.GET, "/api/institution/resources").hasAuthority("INSTITUTION")
            .antMatchers("/api/institution/event/allocate-resources").hasAuthority("INSTITUTION")
            .antMatchers(HttpMethod.DELETE, "/api/institution/event/**").hasAuthority("INSTITUTION")
            .antMatchers(HttpMethod.GET, "/api/institution/registrations/count").hasAuthority("INSTITUTION")

            // EDUCATOR APIs
            .antMatchers("/api/educator/agenda").hasAuthority("EDUCATOR")
            .antMatchers("/api/educator/update-material/**").hasAuthority("EDUCATOR")

            // STUDENT APIs
            .antMatchers("/api/student/register/**").hasAuthority("STUDENT")
            .antMatchers("/api/student/registration-status/**").hasAnyAuthority("INSTITUTION", "STUDENT")

            .anyRequest().authenticated()
            .and()

            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(
                jwtRequestFilter,
                UsernamePasswordAuthenticationFilter.class
        );
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}