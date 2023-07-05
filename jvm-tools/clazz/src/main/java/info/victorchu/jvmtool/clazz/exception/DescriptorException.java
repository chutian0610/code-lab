package info.victorchu.jvmtool.clazz.exception;

public class DescriptorException extends ClassFileFormatException {
    public DescriptorException() {
        super();
    }

    public DescriptorException(String message) {
        super(message);
    }

    public DescriptorException(String message, Throwable cause) {
        super(message, cause);
    }

    public DescriptorException(Throwable cause) {
        super(cause);
    }

    public DescriptorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
