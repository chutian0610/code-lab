package info.victorchu.snippets.antlr;

import lombok.Getter;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Objects.requireNonNull;

/**
 * Manage ANTLR cache for lexer and parser.
 * @see <a href="https://github.com/antlr/antlr4/issues/499">ANTLR4 Cache Issue</a>
 */
public class AntlrCacheManager<L extends Lexer, P extends Parser> {

    private final AtomicReference<AntlrATNCacheFields> lexerCache;
    private final AtomicReference<AntlrATNCacheFields> parserCache;
    @Getter
    private final boolean enableLexerCacheManage;
    @Getter
    private final boolean enableParserCacheManage;
    private final CacheRefreshStrategy lexerCacheRefreshStrategy;
    private final CacheRefreshStrategy parserCacheRefreshStrategy;
    private final AtomicBoolean initialize = new AtomicBoolean(false);

    public AntlrCacheManager(CacheParserOption parserOption) {
        requireNonNull(parserOption);
        enableLexerCacheManage = Boolean.parseBoolean(parserOption.getProperty(
                CacheParserOption.TypeKey.LEXER, CacheParserOption.TypePropertyKey.ENABLE_CACHE_MANAGE, "false"));
        enableParserCacheManage = Boolean.parseBoolean(parserOption.getProperty(
                CacheParserOption.TypeKey.PARSER, CacheParserOption.TypePropertyKey.ENABLE_CACHE_MANAGE, "false"));
        // cache fields will be initialized in accept method
        lexerCache = new AtomicReference<>();
        parserCache = new AtomicReference<>();
        lexerCacheRefreshStrategy = CacheRefreshStrategyFactory.cacheRefreshStrategy(parserOption, CacheParserOption.TypeKey.LEXER);
        parserCacheRefreshStrategy = CacheRefreshStrategyFactory.cacheRefreshStrategy(parserOption, CacheParserOption.TypeKey.PARSER);

    }

    public void accept(L lexer, P parser) {
        if(!enableLexerCacheManage && !enableParserCacheManage) {
            // no cache manage enabled, do nothing
            return;
        }
        if(initialize.compareAndSet(false, true)) {
            // initialize cache when first time accept
            // antlr cache will be managed in AntlrATNCacheFields
            refreshLexerCache(lexer);
            refreshParserCache(parser);
            return;
        }

        if (isEnableLexerCacheManage() && needRefreshLexer(lexer)) {
            refreshLexerCache(lexer);
        }
        if (isEnableParserCacheManage() && needRefreshParser(parser)) {
            refreshParserCache(parser);
        }
    }
    protected boolean needRefreshLexer(L lexer) {
        return lexerCacheRefreshStrategy.needClearCache(lexerCache.get());
    }

    protected boolean needRefreshParser(P parser) {
        return parserCacheRefreshStrategy.needClearCache(parserCache.get());
    }

    protected void refreshLexerCache(L lexer) {
        lexerCache.set(new AntlrATNCacheFields(lexer.getATN()));
        lexerCache.get().configureLexer(lexer);
    }

    protected void refreshParserCache(P parser) {
        parserCache.set(new AntlrATNCacheFields(parser.getATN()));
        parserCache.get().configureParser(parser);
    }
}
