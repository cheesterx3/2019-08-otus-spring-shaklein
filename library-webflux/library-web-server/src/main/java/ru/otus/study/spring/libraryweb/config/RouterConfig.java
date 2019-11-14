package ru.otus.study.spring.libraryweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.otus.study.spring.libraryweb.handlers.BookHandler;
import ru.otus.study.spring.libraryweb.handlers.RegistryHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {
    @Bean(name = "bookRoute")
    public RouterFunction<ServerResponse> bookRoutes(BookHandler bookHandler) {
        return route()
                .GET("/api/books", bookHandler::getAll)
                .DELETE("/api/books/{id}", bookHandler::delete)
                .POST("/api/books", accept(MediaType.APPLICATION_JSON), bookHandler::saveNew)
                .PUT("/api/books/{id}", accept(MediaType.APPLICATION_JSON), bookHandler::save)
                .build();
    }

    @Bean(name = "registryRoute")
    public RouterFunction<ServerResponse> registryRoutes(RegistryHandler registryHandler) {
        return route()
                .GET("/api/authors", registryHandler::getAuthors)
                .GET("/api/genres", registryHandler::getGenres)
                .build();
    }
}
