package com.zoho.attendance.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;

@Configuration
public class CorsConfig implements CorsConfigurationSource {

    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Collections.singletonList("*")); // Set your allowed origins
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // Set your allowed HTTP methods
        corsConfiguration.setAllowedHeaders(Collections.singletonList("*")); // Set your allowed headers
        corsConfiguration.setMaxAge(3600L);
        return corsConfiguration;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
        CorsConfigurationSource source = new CorsConfig();
        CorsConfiguration corsConfiguration = source.getCorsConfiguration(null); // Pass null as the HttpServletRequest parameter
        if (corsConfiguration == null) {
            corsConfiguration = new CorsConfiguration();
        }
        corsConfiguration.addAllowedOrigin("*"); // Set your allowed origins
        corsConfiguration.addAllowedMethod("*"); // Set your allowed HTTP methods
        corsConfiguration.addAllowedHeader("*"); // Set your allowed headers
        corsConfiguration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", corsConfiguration); // Set your endpoint URL pattern
        CorsFilter corsFilter = new CorsFilter(configurationSource);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(corsFilter);
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}