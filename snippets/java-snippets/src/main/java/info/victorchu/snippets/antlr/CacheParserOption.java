package info.victorchu.snippets.antlr;

import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Predicate;

public class CacheParserOption {
    public enum TypeKey {
        LEXER("lexer"),
        PARSER("parser")
        ;
        private final String key;
        TypeKey(String key) {
            this.key = key;
        }
        public String getKey() {
            return key;
        }
    }
    public enum TypePropertyKey {
        ENABLE_CACHE_MANAGE("enableCacheManage"),
        CACHE_MANAGE_STRATEGY("cacheManageStrategy"),
        CACHE_COUNT_STRATEGY_THRESHOLD("cacheCountStrategyThreshold"),
        CACHE_MEMORY_STRATEGY_THRESHOLD("cacheMemoryStrategyThreshold"),
        CACHE_MEMORY_STRATEGY_RATIO("cacheMemoryStrategyRatio")
        ;
        private final String key;

        TypePropertyKey(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    public enum DefaultPropertyValue {
        ENABLE_CACHE_MANAGE("false"),
        CACHE_MANAGE_STRATEGY("DEFAULT"),
        CACHE_COUNT_STRATEGY_THRESHOLD("200"),
        CACHE_MEMORY_STRATEGY_THRESHOLD("-1"),
        CACHE_MEMORY_STRATEGY_RATIO("-1")
        ;
        private final String value;
        DefaultPropertyValue(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }
    private final Properties properties;

    public CacheParserOption(Map<String, String> props) {
        properties = new Properties();
        props.forEach((k, v) -> {
            if (k.startsWith(TypeKey.LEXER.getKey()) || k.startsWith(TypeKey.PARSER.getKey())) {
                properties.setProperty(k, v);
            }
        });
    }

    /**
     * Get the property with the specified key.
     *
     * @param typeKey      The type key of the property to retrieve.
     * @param typePropertyKey  The property key of the property to retrieve.
     * @param defaultValue The default value to return if no property with the
     *                     specified key exists.
     * @return The value of the property with the specified key, or the default
     * value if no property with the specified key exists.
     */
    public String getProperty(TypeKey typeKey, TypePropertyKey typePropertyKey, String defaultValue) {
        return properties.getProperty(typeKey.getKey() + "." + typePropertyKey.getKey(), defaultValue);
    }

    /**
     * Get the property with the specified key.
     *
     * @param typeKey      The type key of the property to retrieve.
     * @param typePropertyKey  The property key of the property to retrieve.
     * @param defaultValue The default value to return if no property with the
     *                     specified key exists.
     * @param validator    The validator to apply to the property value.
     * @param errorMessage The error message to throw if the validator fails.
     * @return The value of the property with the specified key, or the default
     * value if no property with the specified key exists.
     */
    public String getProperty(TypeKey typeKey, TypePropertyKey typePropertyKey, String defaultValue, Predicate<String> validator, String errorMessage) {
        Optional<String> value =  Optional.ofNullable(properties.getProperty(typeKey.getKey() + "." + typePropertyKey.getKey()));
        if (value.isPresent()) {
            if (validator.test(value.get())) {
                return value.get();
            } else {
                throw new IllegalArgumentException(errorMessage);
            }
        }
        return value.orElse(defaultValue);
    }
    /**
     * Get the property with the specified key.
     *
     * @param key         The key of the property to retrieve.
     * @param defaultValue The default value to return if no property with the
     *                     specified key exists.
     * @return The value of the property with the specified key, or the default
     * value if no property with the specified key exists.
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Set the property with the specified key to the specified value.
     *
     * @param key   The key of the property to set.
     * @param value The value to set for the property with the specified key.
     */
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
}

