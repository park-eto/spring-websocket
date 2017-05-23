package com.spring.websocket.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.Arrays;

/**
 * Created by seongmin Park on 2017. 5. 23..
 */
@Configuration
public class SessionRepositoryConfig {

    @Bean
    public SessionRepository inMemorySessionRepository() {
        return new MapSessionRepository();
    }

    @Bean
    public FilterRegistrationBean sessionRepositoryFilterRegistration() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new DelegatingFilterProxy(new SessionRepositoryFilter<>(inMemorySessionRepository())));
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
        return filterRegistrationBean;
    }
}
