package com.parvis.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

@Configuration
@EnableJdbcHttpSession(maxInactiveIntervalInSeconds = 3600) // 60 minutes
public class SessionConfig {

    @Bean
    public SessionAuthenticationFilter sessionAuthenticationFilter() {
        return new SessionAuthenticationFilter();
    }

    @Bean
    public FilterRegistrationBean<SessionAuthenticationFilter> sessionFilter() {
        FilterRegistrationBean<SessionAuthenticationFilter> bean =
                new FilterRegistrationBean<>();

        bean.setFilter(new SessionAuthenticationFilter());
        bean.addUrlPatterns("/api/v1/*"); // apply ONLY to valid API paths

        return bean;
    }
}
