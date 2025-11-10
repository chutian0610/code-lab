package info.victorchu.snippets.antlr;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

import static info.victorchu.snippets.utils.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

public class AntlrATNCacheFields {
    private final ATN atn;
    private final PredictionContextCache predictionContextCache;
    private final DFA[] decisionToDFA;

    public AntlrATNCacheFields(ATN atn)
    {
        this.atn = requireNonNull(atn, "atn is null");
        this.predictionContextCache = new PredictionContextCache();
        this.decisionToDFA = createDecisionToDFA(atn);
    }

    public int computeDFACacheNumStates()
    {
        int numStates = 0;
        for (DFA dfa : decisionToDFA) {
            numStates += dfa.getStates().size();
        }
        return numStates;
    }

    @SuppressWarnings("ObjectEquality")
    public void configureLexer(Lexer lexer)
    {
        requireNonNull(lexer, "lexer is null");
        // Intentional identity equals comparison
        checkArgument(atn == lexer.getATN(), "Lexer ATN mismatch: expected %s, found %s", atn, lexer.getATN());
        lexer.setInterpreter(new LexerATNSimulator(lexer, atn, decisionToDFA, predictionContextCache));
    }

    @SuppressWarnings("ObjectEquality")
    public void configureParser(Parser parser)
    {
        requireNonNull(parser, "parser is null");
        // Intentional identity equals comparison
        checkArgument(atn == parser.getATN(), "Parser ATN mismatch: expected %s, found %s", atn, parser.getATN());
        parser.setInterpreter(new ParserATNSimulator(parser, atn, decisionToDFA, predictionContextCache));
    }

    private static DFA[] createDecisionToDFA(ATN atn)
    {
        DFA[] decisionToDFA = new DFA[atn.getNumberOfDecisions()];
        for (int i = 0; i < decisionToDFA.length; i++) {
            decisionToDFA[i] = new DFA(atn.getDecisionState(i), i);
        }
        return decisionToDFA;
    }
}