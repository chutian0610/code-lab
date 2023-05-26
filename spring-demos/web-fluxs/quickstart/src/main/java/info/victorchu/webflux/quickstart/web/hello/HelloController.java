package info.victorchu.webflux.quickstart.web.hello;

import info.victorchu.webflux.quickstart.model.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * web接口的注解定义方式
 */
@RestController
public class HelloController {

    @GetMapping("/v2/hello")
    public Mono<Response<?>> hello() {
        return Mono.just(Response.builder().code(100).message("hello world").build());
    }
}
