package info.victorchu.jvmtool.clazz.exception;

/**
 * Constant Pool Exception
 * @author chutian
 * @version 1.0
 * @since 2020-01-25
 */

public class ConstantPoolException extends ClassFileFormatException {
    public ConstantPoolException() {
        super();
    }

    public ConstantPoolException(String message) {
        super(message);
    }

    public ConstantPoolException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConstantPoolException(Throwable cause) {
        super(cause);
    }

    public ConstantPoolException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
