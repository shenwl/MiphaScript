package translator;

import exceptions.LexicalException;
import org.junit.jupiter.api.Test;
import parser.Parser;
import parser.ast.ASTNode;
import parser.utils.ParserException;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TransFuncTest {
    @Test
    public void testSimpleFunc() throws FileNotFoundException, ParserException, LexicalException, UnsupportedEncodingException {
        ASTNode ast = Parser.fromFile("./example/simple-func.ms");
        Translator translator = new Translator();
        TAProgram program = translator.translate(ast);

//        System.out.println(program.toString());

        String expected = "L0:\n" +
                "FUNC_BEGIN\n" +
                "p0 = a + b\n" +
                "RETURN p0";
        assertEquals(program.toString(), expected);
    }

    @Test
    public void testRecursionFunc() throws FileNotFoundException, ParserException, LexicalException, UnsupportedEncodingException {
        ASTNode ast = Parser.fromFile("./example/recursion-func.ms");
        Translator translator = new Translator();
        TAProgram program = translator.translate(ast);

//        System.out.println(program.toString());

        String expected = "L0:\n" +
                "FUNC_BEGIN\n" +
                "p0 = n <= 0\n" +
                "IF p0 ELSE L1\n" +
                "SP -2\n" +
                "RETURN 1\n" +
                "SP 2\n" +
                "L1:\n" +
                "p3 = n - 1\n" +
                "PARAM p3 0\n" +
                "SP -5\n" +
                "CALL L0\n" +
                "SP 5\n" +
                "p4 = p1 * n\n" +
                "RETURN p4";

        assertEquals(program.toString(), expected);
    }

}
