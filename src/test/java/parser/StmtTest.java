package parser;

import exceptions.LexicalException;
import lexer.Lexer;
import lexer.Token;
import org.junit.jupiter.api.Test;
import parser.ast.*;
import parser.utils.ParserException;
import parser.utils.ParserUtils;
import parser.utils.PeekTokenIterator;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StmtTest {
    private PeekTokenIterator createTokenIt(String src) throws LexicalException {
        Lexer lexer = new Lexer();
        ArrayList<Token> tokens = lexer.analyse(src.chars().mapToObj(c -> (char) c));
        return new PeekTokenIterator(tokens.stream());
    }

    @Test
    public void test_declareStmtParse() throws LexicalException, ParserException {
        PeekTokenIterator it = createTokenIt("var i = 100 * 2");
        ASTNode stmt = DeclareStmt.parse( it);
        assertEquals("i 100 2 * =", ParserUtils.toPostfixExpression(stmt));
    }

    @Test
    public void test_assignStmtParse() throws LexicalException, ParserException {
        PeekTokenIterator it = createTokenIt("i = 100 * 2");
        ASTNode stmt = AssignStmt.parse(it);
        assertEquals("i 100 2 * =", ParserUtils.toPostfixExpression(stmt));
    }

    @Test
    public void test_ifStmtParse() throws LexicalException, ParserException {
        PeekTokenIterator it = createTokenIt("if (a) {\n" +
                " a = 1 \n" +
                "}"
        );
        IfStmt stmt = (IfStmt) IfStmt.parse(it);
        Variable expr = (Variable) stmt.getChild(0);
        Block block = (Block) stmt.getChild(1);
        AssignStmt assignStmt = (AssignStmt) block.getChild(0);

        assertEquals("a", expr.getLexeme().getValue());
        assertEquals("=", assignStmt.getLexeme().getValue());
    }

    @Test
    public void test_ifElseStmtParse() throws LexicalException, ParserException {
        PeekTokenIterator it = createTokenIt("if (a) {\n" +
                " a = 1 \n" +
                "} else { \n" +
                "a = 2\n" +
                "a = a * 3 \n" +
                "}"
        );
        IfStmt stmt = (IfStmt) IfStmt.parse(it);
        Variable expr = (Variable) stmt.getChild(0);
        Block block = (Block) stmt.getChild(1);
        AssignStmt assignStmt = (AssignStmt) block.getChild(0);
        Block elseBlock = (Block) stmt.getChild(2);
        AssignStmt elseAssignStmt1 = (AssignStmt) elseBlock.getChild(0);
        AssignStmt elseAssignStmt2 = (AssignStmt) elseBlock.getChild(1);


        assertEquals("a", expr.getLexeme().getValue());
        assertEquals("=", assignStmt.getLexeme().getValue());
        assertEquals("=", elseAssignStmt1.getLexeme().getValue());
        assertEquals(2, elseBlock.getChildren().size());
    }

    @Test
    public void test_func() throws LexicalException, ParserException, FileNotFoundException, UnsupportedEncodingException {
        ArrayList<Token> tokens = Lexer.fromFile("./example/function.ms");
        FunctionDeclareStmt functionStmt = (FunctionDeclareStmt) Stmt.parseStmt(new PeekTokenIterator(tokens.stream()));

        // 验证参数
        FunctionArgs args = (FunctionArgs) functionStmt.getArgs();
        assertEquals("a", args.getChild(0).getLexeme().getValue());
        assertEquals("b", args.getChild(1).getLexeme().getValue());
        // 验证函数类型
        assertEquals("int", functionStmt.getFunctionType());
        // 函数声明变量
        Variable functionVariable = functionStmt.getFunctionVariable();
        assertEquals("sum", functionVariable.getLexeme().getValue());

        Block block = functionStmt.getBlock();
        assertTrue(block.getChild(0) instanceof ReturnStmt);
    }

    @Test
    public void test_recursionFunc() throws LexicalException, ParserException, FileNotFoundException, UnsupportedEncodingException {
        ArrayList<Token> tokens = Lexer.fromFile("./example/recursion.ms");
        FunctionDeclareStmt functionStmt = (FunctionDeclareStmt) Stmt.parseStmt(new PeekTokenIterator(tokens.stream()));

        assertEquals("func fact args block", ParserUtils.toBFSString(functionStmt, 4));
        assertEquals("args n", ParserUtils.toBFSString( functionStmt.getArgs(), 2));
        assertEquals("block if return", ParserUtils.toBFSString(functionStmt.getBlock(), 3));
    }
}
