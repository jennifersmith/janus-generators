// Generated from grammars/BasicRegex.g4 by ANTLR 4.0
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class BasicRegexLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		BACK_OPEN_PAREN=1, BACK_CLOSE_PAREN=2, L_ANCHOR=3, R_ANCHOR=4, BACKREF=5, 
		TODO=6;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"'('", "')'", "'^'", "'$'", "BACKREF", "'todo'"
	};
	public static final String[] ruleNames = {
		"BACK_OPEN_PAREN", "BACK_CLOSE_PAREN", "L_ANCHOR", "R_ANCHOR", "BACKREF", 
		"TODO", "INTEGER"
	};


	public BasicRegexLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "BasicRegex.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\2\4\b#\b\1\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\3"+
		"\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\b\3\b"+
		"\2\t\3\3\1\5\4\1\7\5\1\t\6\1\13\7\1\r\b\1\17\2\1\3\2\3\3\62;!\2\3\3\2"+
		"\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\3\21"+
		"\3\2\2\2\5\23\3\2\2\2\7\25\3\2\2\2\t\27\3\2\2\2\13\31\3\2\2\2\r\34\3\2"+
		"\2\2\17!\3\2\2\2\21\22\7*\2\2\22\4\3\2\2\2\23\24\7+\2\2\24\6\3\2\2\2\25"+
		"\26\7`\2\2\26\b\3\2\2\2\27\30\7&\2\2\30\n\3\2\2\2\31\32\7^\2\2\32\33\5"+
		"\17\b\2\33\f\3\2\2\2\34\35\7v\2\2\35\36\7q\2\2\36\37\7f\2\2\37 \7q\2\2"+
		" \16\3\2\2\2!\"\t\2\2\2\"\20\3\2\2\2\3\2";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
	}
}