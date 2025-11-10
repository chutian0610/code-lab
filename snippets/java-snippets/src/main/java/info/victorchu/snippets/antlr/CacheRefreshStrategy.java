package info.victorchu.snippets.antlr;

import info.victorchu.snippets.utils.Preconditions;

import java.util.concurrent.atomic.AtomicLong;

public interface CacheRefreshStrategy {
    enum CacheRefreshStrategyType {
        DEFAULT,
        COUNT,
        MEMORY;
        public static CacheRefreshStrategyType safeValueOf(String name) {
            try {
                return CacheRefreshStrategyType.valueOf(name);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }
    boolean needClearCache(AntlrATNCacheFields antlrATNCacheFields);

    /**
     * Default cache always refresh strategy.
     * <p>
     * Always clear the cache when the parser or lexer is refreshed.
     */
    static CacheRefreshStrategy defaultStrategy() {
        return new CacheRefreshStrategy() {
            @Override
            public boolean needClearCache(AntlrATNCacheFields antlrATNCacheFields) {
                return true;
            }
        };
    }
    /**
     * Cache refresh strategy based on the number of parser or lexer refresh times.
     * <p>
     * Clear the cache when the parser or lexer is refreshed for the specified number of times.
     *
     * @param dfaCacheThreshold The number of parser or lexer refresh times to clear the cache.
     * @return The cache refresh strategy based on the number of parser or lexer refresh times.
     */
    static CacheRefreshStrategy countClearStrategy(long dfaCacheThreshold) {
        Preconditions.checkArgument(dfaCacheThreshold > 0, "dfaCacheThreshold must be greater than 0");
        return new CacheRefreshStrategy() {
            private final AtomicLong counter = new AtomicLong(0);

            @Override
            public boolean needClearCache(AntlrATNCacheFields antlrATNCacheFields) {
                return 0 == counter.incrementAndGet() % dfaCacheThreshold;
            }
        };
    }
    /**
     * Cache refresh strategy based on the memory usage of the DFA cache.
     * <p>
     * Clear the cache when the estimated memory usage of the DFA cache exceeds the specified threshold.
     *
     * @param cacheMemoryUseThreshold The threshold of the number of DFA states to clear the cache.
     * @param cacheMemoryRatio The threshold of the memory usage ratio to clear the cache.
     * @return The cache refresh strategy based on the memory usage of the DFA cache.
     */
    static CacheRefreshStrategy memoryClearStrategy(long cacheMemoryUseThreshold, double cacheMemoryRatio) {
        return new CacheRefreshStrategy() {
            // Approximation based on experiments.
            // Used to estimate the size of the DFA cache for the `cacheMemoryRatio` threshold.
            final long BYTES_PER_DFA_STATE = 9700;
            private long getMaxMemory() {
                return Runtime.getRuntime().maxMemory();
            }

            @Override
            public boolean needClearCache(AntlrATNCacheFields antlrATNCacheFields) {
                long dfaCacheNumStates = antlrATNCacheFields.computeDFACacheNumStates();
                boolean staticThresholdExceeded = false;
                if(cacheMemoryUseThreshold >=0){
                    staticThresholdExceeded = dfaCacheNumStates >= cacheMemoryUseThreshold;
                }
                double estCacheBytes = dfaCacheNumStates * BYTES_PER_DFA_STATE;
                boolean dynamicThresholdExceeded = false;
                if(cacheMemoryRatio >=0 ){
                    dynamicThresholdExceeded = estCacheBytes > (getMaxMemory() * cacheMemoryRatio/100);
                }

                return staticThresholdExceeded || dynamicThresholdExceeded;
            }
        };
    }
}