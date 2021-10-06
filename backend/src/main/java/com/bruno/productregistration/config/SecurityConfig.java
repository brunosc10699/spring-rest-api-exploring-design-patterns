package com.bruno.productregistration.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final Environment environment;

    private static final String[] PUBLIC_MATCHERS = {
            "/h2-console/**",
            "/actuator/health"
    };

    private static final String[] PUBLIC_MATCHERS_GET = {
            "/api/v1/appliances/**",
            "/api/v1/consumptions/**"
    };

    private static final String[] PUBLIC_MATCHERS_POST = {
            "/api/v1/appliances/**",
            "/api/v1/consumptions/**"
    };

    private static final String[] PUBLIC_MATCHERS_PUT = {
            "/api/v1/appliances/**",
            "/api/v1/consumptions/**"
    };

    private static final String[] PUBLIC_MATCHERS_DELETE = {
            "/api/v1/appliances/**",
            "/api/v1/consumptions/**"
    };

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        if (Arrays.asList(environment.getActiveProfiles()).contains("test"))
            httpSecurity.headers().frameOptions().disable();
        httpSecurity.cors().and().csrf().disable();
        if (!Arrays.asList(environment.getActiveProfiles()).contains("prod")) {
            httpSecurity.authorizeRequests().anyRequest().permitAll();
        } else {
            httpSecurity.authorizeRequests()
                    .antMatchers(PUBLIC_MATCHERS)
                    .permitAll()
                    .antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET)
                    .permitAll()
                    .anyRequest()
                    .authenticated();
        }
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
