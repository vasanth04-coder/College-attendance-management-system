package com.collegemanagement.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig
{
    @Autowired
    JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder encoder()
    {
       return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception
    {
       return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity.csrf(csrf-> csrf.disable());

        httpSecurity.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        httpSecurity.authorizeHttpRequests
                (auth->auth
                                .requestMatchers("/users/superAdmin/login").permitAll()
                                .requestMatchers("/users/admin/login").permitAll()
                                .requestMatchers("/users/staff/login").permitAll()
                                .requestMatchers("/users/student/register").permitAll()
                                .requestMatchers("/users/student/login").permitAll()

                                .requestMatchers(HttpMethod.POST,"/users/superAdmin/**").hasRole("SUPER_ADMIN")
                                .requestMatchers(HttpMethod.GET,"/users/superAdmin/**").hasRole("SUPER_ADMIN")
                                .requestMatchers(HttpMethod.POST,"/users/admin/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST,"/users/staff/**").hasRole("STAFF")
                                .requestMatchers(HttpMethod.GET,"/users/staff/**").hasRole("STAFF")

                        .anyRequest().authenticated()
                );



        httpSecurity.addFilterBefore(
                jwtFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        return httpSecurity.build();
    }

}