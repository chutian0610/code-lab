package info.victorchu.demos.guice.quickstart;

import javax.inject.Inject;

/**
 * @author victorchu
 */
public class Greeter {
    private final String message;
    private final int count;

    /**
     *   Greeter declares that it needs a string message and an integer
     *   representing the number of time the message to be printed.
     *   The @Inject annotation marks this constructor as eligible to be used by
     *   Guice.
     */
    @Inject
    Greeter(@Message String message,
            // 此处的@Message标识注入的的String实例的key是Message.class
            @Count int count) {
        this.message = message;
        this.count = count;
    }

    void sayHello() {
        for (int i=0; i < count; i++) {
            System.out.println(message);
        }
    }
}
