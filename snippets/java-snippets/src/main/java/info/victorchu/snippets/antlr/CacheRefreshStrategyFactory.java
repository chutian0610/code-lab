package info.victorchu.snippets.antlr;

import info.victorchu.snippets.utils.Preconditions;

import java.util.function.Predicate;

public class CacheRefreshStrategyFactory {
    public static CacheRefreshStrategy cacheRefreshStrategy(CacheParserOption parserOption, CacheParserOption.TypeKey typeKey) {
        Preconditions.checkNotNull(typeKey, "typeKey must not be null");
        String strategy = parserOption.getProperty(typeKey
                , CacheParserOption.TypePropertyKey.CACHE_MANAGE_STRATEGY,
                CacheParserOption.DefaultPropertyValue.CACHE_MANAGE_STRATEGY.getValue());
        CacheRefreshStrategy.CacheRefreshStrategyType type = CacheRefreshStrategy.CacheRefreshStrategyType.safeValueOf(strategy);
        Preconditions.checkNotNull(type, "Unknown cache refresh strategy type: %s", strategy);
        switch (type) {
            case DEFAULT:
                return CacheRefreshStrategy.defaultStrategy();
            case COUNT:
                String countThreshold = parserOption.getProperty(typeKey,
                        CacheParserOption.TypePropertyKey.CACHE_COUNT_STRATEGY_THRESHOLD,
                        CacheParserOption.DefaultPropertyValue.CACHE_COUNT_STRATEGY_THRESHOLD.getValue(),
                        (Predicate<String>) input -> {
                            if (input != null) {
                                long thresholdValue = Long.parseLong(input);
                                return thresholdValue >= 0;
                            }
                            return false;
                        },"thresholdValue must be greater than 0");

                return CacheRefreshStrategy.countClearStrategy(Long.parseLong(countThreshold));
            case MEMORY:
                String memoryThreshold = parserOption.getProperty(typeKey,
                        CacheParserOption.TypePropertyKey.CACHE_MEMORY_STRATEGY_THRESHOLD,
                        CacheParserOption.DefaultPropertyValue.CACHE_COUNT_STRATEGY_THRESHOLD.getValue(),
                        (Predicate<String>) input -> {
                            if (input != null) {
                                long thresholdValue = Long.parseLong(input);
                                return thresholdValue >= 0;
                            }
                            return false;
                        },"thresholdValue must be greater than or equal to 0");
                String memoryRatio = parserOption.getProperty(typeKey,
                        CacheParserOption.TypePropertyKey.CACHE_MEMORY_STRATEGY_RATIO,
                        CacheParserOption.DefaultPropertyValue.CACHE_MEMORY_STRATEGY_RATIO.getValue(),
                        (Predicate<String>) input -> {
                            if (input != null) {
                                long thresholdValue = Long.parseLong(input);
                                return 100 >= thresholdValue && thresholdValue >= 0;
                            }
                            return false;
                        },"thresholdValue must be greater than or equal to 0 and less than or equal to 100");
                return CacheRefreshStrategy.memoryClearStrategy(Long.parseLong(memoryThreshold), Double.parseDouble(memoryRatio));
            default:
                throw new IllegalArgumentException("Unknown cache refresh strategy type: " + type);
        }
    }
}
