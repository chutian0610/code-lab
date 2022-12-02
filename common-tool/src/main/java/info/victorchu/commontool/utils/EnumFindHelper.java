package info.victorchu.commontool.utils;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Enum查询工具类
 * @author victorchu
 * @date 2022/8/5 22:12
 */
public class EnumFindHelper<T extends Enum<T>, K> {

    @FunctionalInterface
    public interface EnumKeyGetter<T extends Enum<T>, K> {
        /**
         * 从Enum中提取出Key值
         * @param enumValue
         * @return
         */
        K getKey(T enumValue);

    }

    /**
     * 不可变Map
     */
    private Map<K, T> map;

    public EnumFindHelper(Class<T> clazz, EnumKeyGetter<T, K> keyGetter) {
        Map<K,T> builder = new HashMap<>();
        for (T enumValue : EnumSet.allOf(clazz)) {
            builder.put(keyGetter.getKey(enumValue), enumValue);
        }
        map = Collections.unmodifiableMap(builder);
    }

    @Nullable
    public Optional<T> find(K key) {
        return Optional.ofNullable(map.get(key));
    }
}
