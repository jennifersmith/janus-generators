// Generated from grammars/BasicRegex.g4 by ANTLR 4.0
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class BasicRegexParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		BACK_OPEN_PAREN=1, BACK_CLOSE_PAREN=2, L_ANCHOR=3, R_ANCHOR=4, BACKREF=5, 
		TODO=6;
	public static final String[] tokenNames = {
		"<INVALID>", "'('", "')'", "'^'", "'$'", "BACKREF", "'todo'"
	};
	public static final int
		RULE_r = 0, RULE_basic_reg_exp = 1, RULE_re_expression = 2, RULE_simple_re = 3, 
		RULE_nondupl_re = 4, RULE_re_dupl_symbol = 5, RULE_one_character_re = 6;
	public static final String[] ruleNames = {
		"r", "basic_reg_exp", "re_expression", "simple_re", "nondupl_re", "re_dupl_symbol", 
		"one_character_re"
	};

	@Override
	public String getGrammarFileName() { return "BasicRegex.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public BasicRegexParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class RContext extends ParserRuleContext {
		public Basic_reg_expContext basic_reg_exp() {
			return getRuleContext(Basic_reg_expContext.class,0);
		}
		public RContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_r; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BasicRegexListener ) ((BasicRegexListener)listener).enterR(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BasicRegexListener ) ((BasicRegexListener)listener).exitR(this);
		}
	}

	public final RContext r() throws RecognitionException {
		RContext _localctx = new RContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_r);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(14); basic_reg_exp();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Basic_reg_expContext extends ParserRuleContext {
		public TerminalNode R_ANCHOR() { return getToken(BasicRegexParser.R_ANCHOR, 0); }
		public TerminalNode L_ANCHOR() { return getToken(BasicRegexParser.L_ANCHOR, 0); }
		public Re_expressionContext re_expression() {
			return getRuleContext(Re_expressionContext.class,0);
		}
		public Basic_reg_expContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_basic_reg_exp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BasicRegexListener ) ((BasicRegexListener)listener).enterBasic_reg_exp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BasicRegexListener ) ((BasicRegexListener)listener).exitBasic_reg_exp(this);
		}
	}

	public final Basic_reg_expContext basic_reg_exp() throws RecognitionException {
		Basic_reg_expContext _localctx = new Basic_reg_expContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_basic_reg_exp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(17);
			_la = _input.LA(1);
			if (_la==L_ANCHOR) {
				{
				setState(16); match(L_ANCHOR);
				}
			}

			setState(19); re_expression();
			setState(21);
			_la = _input.LA(1);
			if (_la==R_ANCHOR) {
				{
				setState(20); match(R_ANCHOR);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Re_expressionContext extends ParserRuleContext {
		public List<Simple_reContext> simple_re() {
			return getRuleContexts(Simple_reContext.class);
		}
		public Simple_reContext simple_re(int i) {
			return getRuleContext(Simple_reContext.class,i);
		}
		public Re_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_re_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BasicRegexListener ) ((BasicRegexListener)listener).enterRe_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BasicRegexListener ) ((BasicRegexListener)listener).exitRe_expression(this);
		}
	}

	public final Re_expressionContext re_expression() throws RecognitionException {
		Re_expressionContext _localctx = new Re_expressionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_re_expression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(24); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(23); simple_re();
				}
				}
				setState(26); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BACK_OPEN_PAREN) | (1L << BACKREF) | (1L << TODO))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Simple_reContext extends ParserRuleContext {
		public Re_dupl_symbolContext re_dupl_symbol() {
			return getRuleContext(Re_dupl_symbolContext.class,0);
		}
		public Nondupl_reContext nondupl_re() {
			return getRuleContext(Nondupl_reContext.class,0);
		}
		public Simple_reContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simple_re; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BasicRegexListener ) ((BasicRegexListener)listener).enterSimple_re(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BasicRegexListener ) ((BasicRegexListener)listener).exitSimple_re(this);
		}
	}

	public final Simple_reContext simple_re() throws RecognitionException {
		Simple_reContext _localctx = new Simple_reContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_simple_re);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(28); nondupl_re();
			setState(30);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				{
				setState(29); re_dupl_symbol();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Nondupl_reContext extends ParserRuleContext {
		public One_character_reContext one_character_re() {
			return getRuleContext(One_character_reContext.class,0);
		}
		public TerminalNode BACK_CLOSE_PAREN() { return getToken(BasicRegexParser.BACK_CLOSE_PAREN, 0); }
		public TerminalNode BACK_OPEN_PAREN() { return getToken(BasicRegexParser.BACK_OPEN_PAREN, 0); }
		public TerminalNode BACKREF() { return getToken(BasicRegexParser.BACKREF, 0); }
		public Re_expressionContext re_expression() {
			return getRuleContext(Re_expressionContext.class,0);
		}
		public Nondupl_reContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nondupl_re; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BasicRegexListener ) ((BasicRegexListener)listener).enterNondupl_re(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BasicRegexListener ) ((BasicRegexListener)listener).exitNondupl_re(this);
		}
	}

	public final Nondupl_reContext nondupl_re() throws RecognitionException {
		Nondupl_reContext _localctx = new Nondupl_reContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_nondupl_re);
		try {
			setState(40);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(32); one_character_re();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(33); match(BACK_OPEN_PAREN);
				setState(34); re_expression();
				setState(35); match(BACK_CLOSE_PAREN);
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(37); match(BACK_OPEN_PAREN);
				setState(38); match(BACK_CLOSE_PAREN);
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(39); match(BACKREF);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Re_dupl_symbolContext extends ParserRuleContext {
		public TerminalNode TODO() { return getToken(BasicRegexParser.TODO, 0); }
		public Re_dupl_symbolContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_re_dupl_symbol; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BasicRegexListener ) ((BasicRegexListener)listener).enterRe_dupl_symbol(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BasicRegexListener ) ((BasicRegexListener)listener).exitRe_dupl_symbol(this);
		}
	}

	public final Re_dupl_symbolContext re_dupl_symbol() throws RecognitionException {
		Re_dupl_symbolContext _localctx = new Re_dupl_symbolContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_re_dupl_symbol);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(42); match(TODO);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class One_character_reContext extends ParserRuleContext {
		public TerminalNode TODO() { return getToken(BasicRegexParser.TODO, 0); }
		public One_character_reContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_one_character_re; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BasicRegexListener ) ((BasicRegexListener)listener).enterOne_character_re(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BasicRegexListener ) ((BasicRegexListener)listener).exitOne_character_re(this);
		}
	}

	public final One_character_reContext one_character_re() throws RecognitionException {
		One_character_reContext _localctx = new One_character_reContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_one_character_re);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(44); match(TODO);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\2\3\b\61\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\3\2"+
		"\3\2\3\3\5\3\24\n\3\3\3\3\3\5\3\30\n\3\3\4\6\4\33\n\4\r\4\16\4\34\3\5"+
		"\3\5\5\5!\n\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6+\n\6\3\7\3\7\3\b\3\b"+
		"\3\b\2\t\2\4\6\b\n\f\16\2\2\60\2\20\3\2\2\2\4\23\3\2\2\2\6\32\3\2\2\2"+
		"\b\36\3\2\2\2\n*\3\2\2\2\f,\3\2\2\2\16.\3\2\2\2\20\21\5\4\3\2\21\3\3\2"+
		"\2\2\22\24\7\5\2\2\23\22\3\2\2\2\23\24\3\2\2\2\24\25\3\2\2\2\25\27\5\6"+
		"\4\2\26\30\7\6\2\2\27\26\3\2\2\2\27\30\3\2\2\2\30\5\3\2\2\2\31\33\5\b"+
		"\5\2\32\31\3\2\2\2\33\34\3\2\2\2\34\32\3\2\2\2\34\35\3\2\2\2\35\7\3\2"+
		"\2\2\36 \5\n\6\2\37!\5\f\7\2 \37\3\2\2\2 !\3\2\2\2!\t\3\2\2\2\"+\5\16"+
		"\b\2#$\7\3\2\2$%\5\6\4\2%&\7\4\2\2&+\3\2\2\2\'(\7\3\2\2(+\7\4\2\2)+\7"+
		"\7\2\2*\"\3\2\2\2*#\3\2\2\2*\'\3\2\2\2*)\3\2\2\2+\13\3\2\2\2,-\7\b\2\2"+
		"-\r\3\2\2\2./\7\b\2\2/\17\3\2\2\2\7\23\27\34 *";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
	}
}