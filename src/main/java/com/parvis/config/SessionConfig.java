package com.parvis.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

/**
 * Configures JDBC-backed HTTP session management and registers the
 * {@link SessionAuthenticationFilter} used to protect API endpoints.
 *
 * <p>This configuration enables persistent and scalable session storage
 * backed by a relational database via {@link EnableJdbcHttpSession}.
 * Sessions automatically expire after 60 minutes of inactivity.</p>
 *
 * <p>The registered {@link SessionAuthenticationFilter} enforces session-based
 * authentication by verifying that an HTTP session exists and contains a valid
 * "user" attribute before allowing access to secured API routes.</p>
 */
@Configuration
@EnableJdbcHttpSession(maxInactiveIntervalInSeconds = 3600) // 60 minutes
public class SessionConfig {

    /**
     * Creates the {@link SessionAuthenticationFilter} bean.
     *
     * <p>This filter is responsible for validating authenticated sessions on
     * incoming requests. If no valid session or user attribute is found, the
     * filter returns an HTTP 401 Unauthorized response.</p>
     *
     * @return a new {@link SessionAuthenticationFilter} instance
     */
    @Bean
    public SessionAuthenticationFilter sessionAuthenticationFilter() {
        return new SessionAuthenticationFilter();
    }

    /**
     * Registers the {@link SessionAuthenticationFilter} with the servlet container.
     *
     * <p>The filter is applied only to the {@code /api/v1/*} URL pattern to ensure
     * that authentication is enforced exclusively on backend API endpoints.</p>
     *
     * <p>The filter order is explicitly set to {@code 2} so that cross-origin
     * resource sharing (CORS) filters and other framework-level filters execute
     * first. This prevents unauthorized blocking of CORS preflight (OPTIONS)
     * requests when using HttpOnly session cookies.</p>
     *
     * @return a configured {@link FilterRegistrationBean} for the session filter
     */
    @Bean
    public FilterRegistrationBean<SessionAuthenticationFilter> sessionFilter() {
        FilterRegistrationBean<SessionAuthenticationFilter> bean =
                new FilterRegistrationBean<>();

        bean.setFilter(new SessionAuthenticationFilter());
        bean.addUrlPatterns("/api/v1/*"); // apply ONLY to valid API paths
        bean.setOrder(2); // ensure CORS runs before session authentication

        return bean;
    }
}
