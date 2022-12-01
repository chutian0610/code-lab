package info.victorchu.commontool.utils;

import com.google.common.collect.ImmutableMap;

import javax.annotation.Nullable;
import java.util.EnumSet;
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
    private ImmutableMap<K, T> map;

    public EnumFindHelper(Class<T> clazz, EnumKeyGetter<T, K> keyGetter) {
        ImmutableMap.Builder<K,T> builder = ImmutableMap.builder();
        for (T enumValue : EnumSet.allOf(clazz)) {
            builder.put(keyGetter.getKey(enumValue), enumValue);
        }
        map = builder.build();
    }

    @Nullable
    public Optional<T> find(K key) {
        return Optional.ofNullable(map.get(key));
    }
}
