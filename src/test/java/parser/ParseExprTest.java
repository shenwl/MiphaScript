package parser;

import exceptions.LexicalException;
import lexer.Lexer;
import lexer.Token;
import org.junit.jupiter.api.Test;
import parser.ast.ASTNode;
import parser.ast.Expr;
import parser.utils.ParserException;
import parser.utils.ParserUtils;
import parser.utils.PeekTokenIterator;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParseExprTest {
    private ASTNode createExpr(String src) throws LexicalException, ParserException {
        Lexer lexer = new Lexer();
        ArrayList<Token> tokens = lexer.analyse(src.chars().mapToObj(c -> (char) c));
        PeekTokenIterator it = new PeekTokenIterator(tokens.stream());
        return Expr.parse(it);
    }

    @Test
    public void simple() throws LexicalException, ParserException {
        ASTNode expr = createExpr("1+1+1");
        assertEquals("1 1 1 + +", ParserUtils.toPostfixExpression(expr));
    }

    @Test
    public void simple1() throws LexicalException, ParserException {
        ASTNode expr = createExpr("\"1\" == \"\"");
        assertEquals("\"1\" \"\" ==", ParserUtils.toPostfixExpression(expr));
    }

    @Test
    public void complex() throws LexicalException, ParserException {
        ASTNode expr1 = createExpr("1+2*3");
        ASTNode expr2 = createExpr("1*2+3");
        ASTNode expr3 = createExpr("10*(7+4)");
        ASTNode expr4 = createExpr("(1*2!=7)==3!=4*5+6");
        assertEquals("1 2 3 * +", ParserUtils.toPostfixExpression(expr1));
        assertEquals("1 2 * 3 +", ParserUtils.toPostfixExpression(expr2));
        assertEquals("10 7 4 + *", ParserUtils.toPostfixExpression(expr3));
        assertEquals("1 2 * 7 != 3 4 5 * 6 + != ==", ParserUtils.toPostfixExpression(expr4));
    }
}
