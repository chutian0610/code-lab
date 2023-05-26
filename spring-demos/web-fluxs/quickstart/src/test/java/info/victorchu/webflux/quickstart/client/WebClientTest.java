package info.victorchu.webflux.quickstart.client;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebClientTest {
    private static WebClient client;

    @BeforeAll
    public static void start() {
        client = WebClient.create("http://localhost:8080");
    }

    @Test
    public void testHelloV1() {
        String result = client.get().uri("/v1/hello").accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(String.class).block();
        System.out.println(result);
    }
    @Test
    public void testHelloV2() {
        String result = client.get().uri("/v2/hello").accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(String.class).block();
        System.out.println(result);
    }

}
