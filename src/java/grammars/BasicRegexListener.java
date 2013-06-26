// Generated from grammars/BasicRegex.g4 by ANTLR 4.0
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.Token;

public interface BasicRegexListener extends ParseTreeListener {
	void enterOne_character_re(BasicRegexParser.One_character_reContext ctx);
	void exitOne_character_re(BasicRegexParser.One_character_reContext ctx);

	void enterR(BasicRegexParser.RContext ctx);
	void exitR(BasicRegexParser.RContext ctx);

	void enterRe_dupl_symbol(BasicRegexParser.Re_dupl_symbolContext ctx);
	void exitRe_dupl_symbol(BasicRegexParser.Re_dupl_symbolContext ctx);

	void enterSimple_re(BasicRegexParser.Simple_reContext ctx);
	void exitSimple_re(BasicRegexParser.Simple_reContext ctx);

	void enterRe_expression(BasicRegexParser.Re_expressionContext ctx);
	void exitRe_expression(BasicRegexParser.Re_expressionContext ctx);

	void enterNondupl_re(BasicRegexParser.Nondupl_reContext ctx);
	void exitNondupl_re(BasicRegexParser.Nondupl_reContext ctx);

	void enterBasic_reg_exp(BasicRegexParser.Basic_reg_expContext ctx);
	void exitBasic_reg_exp(BasicRegexParser.Basic_reg_expContext ctx);
}