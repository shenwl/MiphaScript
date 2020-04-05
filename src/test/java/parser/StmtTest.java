package parser;

import exceptions.LexicalException;
import lexer.Lexer;
import lexer.Token;
import org.junit.jupiter.api.Test;
import parser.ast.ASTNode;
import parser.ast.AssignStmt;
import parser.ast.DeclareStmt;
import parser.utils.ParserException;
import parser.utils.ParserUtils;
import parser.utils.PeekTokenIterator;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StmtTest {
    private PeekTokenIterator createTokenIt(String src) throws LexicalException {
        Lexer lexer = new Lexer();
        ArrayList<Token> tokens = lexer.analyse(src.chars().mapToObj(c -> (char)c));
        return new PeekTokenIterator(tokens.stream());
    }
    @Test
    public void test_declareStmtParse() throws LexicalException, ParserException {
        PeekTokenIterator it = createTokenIt("var i = 100 * 2");
        ASTNode stmt = DeclareStmt.parse(null, it);
        assertEquals("i 100 2 * =", ParserUtils.toPostfixExpression(stmt));
    }
    @Test
    public void test_assignStmtParse() throws LexicalException, ParserException {
        PeekTokenIterator it = createTokenIt("i = 100 * 2");
        ASTNode stmt = AssignStmt.parse(null, it);
        assertEquals("i 100 2 * =", ParserUtils.toPostfixExpression(stmt));
    }
}
