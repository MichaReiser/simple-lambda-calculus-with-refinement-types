// Generated from /Users/micha/OneDrive/Hsr/Master/Seminar 2/simple-lambda-calculus/src/main/resources/LambdaCalculus.g4 by ANTLR 4.5.3
package ch.famreiser.hsr.seminar.generated;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class LambdaCalculusLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		Identifier=10, Operator=11, Int=12, Bool=13, WS=14;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"Identifier", "Operator", "Int", "Bool", "WS"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'('", "')'", "'=>'", "'let'", "'='", "'in'", "'if'", "'then'", 
		"'else'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, "Identifier", 
		"Operator", "Int", "Bool", "WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public LambdaCalculusLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "LambdaCalculus.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\20c\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\3\2\3\2\3\3\3\3\3\4\3\4\3\4\3"+
		"\5\3\5\3\5\3\5\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\n"+
		"\3\n\3\n\3\n\3\n\3\13\3\13\7\13?\n\13\f\13\16\13B\13\13\3\f\3\f\3\f\3"+
		"\f\3\f\3\f\3\f\5\fK\n\f\3\r\6\rN\n\r\r\r\16\rO\3\16\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\5\16[\n\16\3\17\6\17^\n\17\r\17\16\17_\3\17\3\17"+
		"\2\2\20\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17"+
		"\35\20\3\2\6\4\2\62;c|\7\2,-//\61\61>>@@\3\2\62;\5\2\13\f\17\17\"\"i\2"+
		"\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2"+
		"\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2"+
		"\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\3\37\3\2\2\2\5!\3\2\2\2\7#\3\2\2"+
		"\2\t&\3\2\2\2\13*\3\2\2\2\r,\3\2\2\2\17/\3\2\2\2\21\62\3\2\2\2\23\67\3"+
		"\2\2\2\25<\3\2\2\2\27J\3\2\2\2\31M\3\2\2\2\33Z\3\2\2\2\35]\3\2\2\2\37"+
		" \7*\2\2 \4\3\2\2\2!\"\7+\2\2\"\6\3\2\2\2#$\7?\2\2$%\7@\2\2%\b\3\2\2\2"+
		"&\'\7n\2\2\'(\7g\2\2()\7v\2\2)\n\3\2\2\2*+\7?\2\2+\f\3\2\2\2,-\7k\2\2"+
		"-.\7p\2\2.\16\3\2\2\2/\60\7k\2\2\60\61\7h\2\2\61\20\3\2\2\2\62\63\7v\2"+
		"\2\63\64\7j\2\2\64\65\7g\2\2\65\66\7p\2\2\66\22\3\2\2\2\678\7g\2\289\7"+
		"n\2\29:\7u\2\2:;\7g\2\2;\24\3\2\2\2<@\4c|\2=?\t\2\2\2>=\3\2\2\2?B\3\2"+
		"\2\2@>\3\2\2\2@A\3\2\2\2A\26\3\2\2\2B@\3\2\2\2CK\t\3\2\2DE\7>\2\2EK\7"+
		"?\2\2FG\7@\2\2GK\7?\2\2HI\7?\2\2IK\7?\2\2JC\3\2\2\2JD\3\2\2\2JF\3\2\2"+
		"\2JH\3\2\2\2K\30\3\2\2\2LN\t\4\2\2ML\3\2\2\2NO\3\2\2\2OM\3\2\2\2OP\3\2"+
		"\2\2P\32\3\2\2\2QR\7V\2\2RS\7T\2\2ST\7W\2\2T[\7G\2\2UV\7H\2\2VW\7C\2\2"+
		"WX\7N\2\2XY\7U\2\2Y[\7G\2\2ZQ\3\2\2\2ZU\3\2\2\2[\34\3\2\2\2\\^\t\5\2\2"+
		"]\\\3\2\2\2^_\3\2\2\2_]\3\2\2\2_`\3\2\2\2`a\3\2\2\2ab\b\17\2\2b\36\3\2"+
		"\2\2\b\2@JOZ_\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}