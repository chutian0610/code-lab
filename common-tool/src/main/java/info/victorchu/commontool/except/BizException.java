package info.victorchu.commontool.except;

/**
 * @Copyright:www.xiaojukeji.com Inc. All rights reserved.
 * @Description:
 * @Date:2022/12/2 10:28
 * @Author:victorchutian
 */
public abstract class BizException extends Exception{
    public int getCode() {
        return code;
    }

    protected int code;
    public BizException (int code,Throwable cause){
        super("BizException, code:"+code+";"+(cause==null?"":cause.getMessage()),cause);
    }
    public BizException (int code,String message,Throwable cause){
        super("BizException, code:"+code+",message:"+message+";"+(cause==null?"":cause.getMessage()),cause);
    }
}
