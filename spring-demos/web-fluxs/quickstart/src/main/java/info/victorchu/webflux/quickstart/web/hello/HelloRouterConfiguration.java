package info.victorchu.webflux.quickstart.web.hello;

import info.victorchu.webflux.quickstart.model.Response;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * web接口的配置定义方式
 */
@Configuration
public class HelloRouterConfiguration {
    @Bean
    public RouterFunction<ServerResponse> monoRouterFunction(HelloHandler helloHandler) {
        return route(GET("/v1/hello").and(accept(APPLICATION_JSON)), helloHandler::hello);
    }

    @Component
    public static class HelloHandler {

        public Mono<ServerResponse> hello(ServerRequest request) {
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(Response.builder().code(100).message("success").build()));
        }
    }

}
