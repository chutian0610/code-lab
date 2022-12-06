package info.victorchu.commontool.except;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Description:
 * @Date:2022/12/2 10:34
 * @Author:victorchutian
 */
public enum BizCode {
    OK(0),
    UNKNOWN_EXCEPTION(-1)
    ;

    private static final Map<Integer,BizCode> byCodes;
    static {
        Map<Integer,BizCode> map = new HashMap<>();
        BizCode[] codes = values();
        for (int i = 0; i < codes.length; i++) {
            BizCode bizCode= codes[i];
            map.put(bizCode.code,bizCode);
        }
        byCodes= Collections.unmodifiableMap(map);
    }
    public static Optional<BizCode> fromCode(Integer code){
        return Optional.ofNullable(byCodes.get(code));
    }

    public final Integer code;
    private BizCode(Integer code){
        this.code = code;
    }
    @Override
    public String toString(){
        return this.name()+" (code "+this.code+")";
    }


}
