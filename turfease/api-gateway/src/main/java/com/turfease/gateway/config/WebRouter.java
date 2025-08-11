package com.turfease.gateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.context.annotation.Bean;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class WebRouter {
    @Bean
    public RouterFunction<ServerResponse> htmlRouter() {
        return route(GET("/"), req -> ServerResponse.ok().contentType(MediaType.TEXT_HTML)
                        .bodyValue(new ClassPathResource("static/index.html")))
                .andRoute(GET("/{path:^(?!api|actuator).*$}"), req -> ServerResponse.ok().contentType(MediaType.TEXT_HTML)
                        .bodyValue(new ClassPathResource("static/index.html")))
                .andRoute(path("/{path:^(?!api|actuator).*$}/**"), req -> ServerResponse.ok().contentType(MediaType.TEXT_HTML)
                        .bodyValue(new ClassPathResource("static/index.html")));
    }
}