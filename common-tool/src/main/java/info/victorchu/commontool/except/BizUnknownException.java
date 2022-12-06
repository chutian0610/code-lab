package info.victorchu.commontool.except;

/**
 * @Description:
 * @Date:2022/12/2 10:42
 * @Author:victorchutian
 */
public class BizUnknownException extends BizException{
    public BizUnknownException(Throwable cause) {
        super(BizCode.UNKNOWN_EXCEPTION.code, cause);
    }

    public BizUnknownException(String message, Throwable cause) {
        super(BizCode.UNKNOWN_EXCEPTION.code, message, cause);
    }
}
