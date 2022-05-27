package info.victorchu.fluentjson.jackson.om;

public class JacksonJsonSerializerException extends RuntimeException {
    public JacksonJsonSerializerException() {
    }

    public JacksonJsonSerializerException(String message) {
        super(message);
    }

    public JacksonJsonSerializerException(String message, Throwable cause) {
        super(message, cause);
    }

    public JacksonJsonSerializerException(Throwable cause) {
        super(cause);
    }

    public JacksonJsonSerializerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
