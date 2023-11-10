package info.victorchu.demos.guice.quickstart;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author victorchu
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface Count {
}
