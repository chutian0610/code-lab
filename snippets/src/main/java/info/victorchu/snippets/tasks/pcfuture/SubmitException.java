package info.victorchu.snippets.tasks.pcfuture;

/**
 * 表示任务提交异常
 * @author victorchu

 */
public class SubmitException extends Exception{
    public SubmitException() {
    }

    public SubmitException(String message) {
        super(message);
    }

    public SubmitException(String message, Throwable cause) {
        super(message, cause);
    }

    public SubmitException(Throwable cause) {
        super(cause);
    }

    public SubmitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}