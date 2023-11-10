package info.victorchu.demos.spring.webflux.quickstart.web.stream;

import info.victorchu.demos.spring.webflux.quickstart.model.Random;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

/**
 * webflux返回 stream 数据
 */
@RestController
public class StreamController {
    @GetMapping(value = "/stream/print",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Random> priceStreamPrint(){
        return Flux.interval(Duration.ofMillis(500))
                .map(l -> new Random(System.currentTimeMillis(), ThreadLocalRandom.current().nextInt(100, 125)))
                .log();
    }

    @GetMapping(value = "/stream/file",produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Random> priceStreamDownload(){
        return Flux.interval(Duration.ofMillis(500))
                .map(l -> new Random(System.currentTimeMillis(), ThreadLocalRandom.current().nextInt(100, 125)))
                .log();
    }


}
