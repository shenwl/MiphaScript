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
}
