package info.victorchu.commontool.utils;

import com.google.common.collect.ImmutableMap;

import javax.annotation.Nullable;
import java.util.EnumSet;

/**
 * @author victorchu
 * @date 2022/8/5 22:12
 */
public class EnumFindHelper<T extends Enum<T>, K> {

    @FunctionalInterface
    public interface EnumKeyGetter<T extends Enum<T>, K> {
        K getKey(T enumValue);

    }

    private ImmutableMap<K, T> map;

    public EnumFindHelper(Class<T> clazz, EnumKeyGetter<T, K> keyGetter) {
        ImmutableMap.Builder<K,T> builder = ImmutableMap.builder();
        for (T enumValue : EnumSet.allOf(clazz)) {
            builder.put(keyGetter.getKey(enumValue), enumValue);
        }
        map = builder.build();
    }

    @Nullable
    public T find(K key) {
        return map.get(key);
    }
}
