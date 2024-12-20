package org.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${management.server.port}")
    private int managementPort;

    @Value("${server.port}")
    private int apiPort;

    @Value("${page.port}")
    private int pagePort;

    @Autowired
    private MyAuthenticationProvider authProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception { // 配置资源和用户权限匹配
        http.csrf().disable();
        // http.anonymous();
        http
            .authorizeRequests()
            .requestMatchers(forPortAndPath(managementPort, "/actuator/prometheus")).anonymous()
            .requestMatchers(forPortAndPath(apiPort, "/**")).anonymous()
            .antMatchers("/**").hasRole("ADMIN")
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login.html")
            .defaultSuccessUrl("/index.html", true)
            .permitAll()
            .and()
            .logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/login.html")
            .permitAll()
            .and()
            .exceptionHandling().accessDeniedPage("/info/403.html")
            .and()
            .headers()
            .frameOptions().sameOrigin()
            .httpStrictTransportSecurity().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider);
    }

    /**
     * Creates a request matcher which only matches requests for a specific local port and path (using an
     * {@link AntPathRequestMatcher} for the path part).
     *
     * @param   port         the port to match
     * @param   pathPattern  the pattern for the path.
     *
     * @return  the new request matcher.
     */
    private RequestMatcher forPortAndPath(final int port, @Nonnull final String pathPattern) {
        return new AndRequestMatcher(forPort(port), new AntPathRequestMatcher(pathPattern));
    }

    /**
     * Creates a request matcher which only matches requests for a specific local port, path and request method (using
     * an {@link AntPathRequestMatcher} for the path part).
     *
     * @param   port         the port to match
     * @param   pathPattern  the pattern for the path.
     * @param   method       the HttpMethod to match. Requests for other methods will not be matched.
     *
     * @return  the new request matcher.
     */
    private RequestMatcher forPortAndPath(final int port, @Nonnull final HttpMethod method,
        @Nonnull final String pathPattern) {
        return new AndRequestMatcher(forPort(port), new AntPathRequestMatcher(pathPattern, method.name()));
    }

    /**
     * A request matcher which matches just a port.
     *
     * @param   port  the port to match.
     *
     * @return  the new matcher.
     */
    private RequestMatcher forPort(final int port) {
        return (HttpServletRequest request) -> { return port == request.getLocalPort(); };
    }

}
