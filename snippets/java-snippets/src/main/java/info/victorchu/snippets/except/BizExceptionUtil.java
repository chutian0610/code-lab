package info.victorchu.snippets.except;

import java.util.Optional;

import static info.victorchu.snippets.utils.ExceptionUtils.getThrowables;

/**
 * BizExceptionUtil.
 * enhance info.victorchu.commontool.utils.ExceptionUtils
 * @Description:
 * @Date:2022/12/2 10:42
 * @Author:victorchutian
 */
public class BizExceptionUtil {

    /**
     * 搜索第一个BizException 异常
     * @param throwable
     * @param code  如果不为空，BizCode 搜索
     * @param type  如果不为空，类型搜索
     * @param fromIndex
     * @return
     */
    private static Optional<BizException> firstBizExceptionOf(final Throwable throwable,
                                                              final Integer code,
                                                              final Class<? extends BizException> type,
                                                              int fromIndex) {
        if (throwable == null) {
            return Optional.empty();
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        final Throwable[] throwables = getThrowables(throwable);
        if (fromIndex >= throwables.length) {
            return Optional.empty();
        }
        boolean subclass = (type != null);
        if (subclass) {
            for (int i = fromIndex; i < throwables.length; i++) {
                if (BizException.class.isAssignableFrom(throwables[i].getClass())) {
                    if(code != null && ((BizException)throwables[i]).getCode() ==code) {
                        return Optional.of(BizException.class.cast(throwables[i]));
                    }else {
                        if(code == null){
                            return Optional.of(BizException.class.cast(throwables[i]));
                        }
                    }
                }
            }
        } else {
            for (int i = fromIndex; i < throwables.length; i++) {
                if (type.equals(throwables[i].getClass())) {
                    if(code != null && ((BizException)throwables[i]).getCode() ==code) {
                        return Optional.of(BizException.class.cast(throwables[i]));
                    }else {
                        if(code == null){
                            return Optional.of(BizException.class.cast(throwables[i]));
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }
}
