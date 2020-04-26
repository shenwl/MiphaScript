package translator;

import exceptions.LexicalException;
import org.junit.jupiter.api.Test;
import parser.Parser;
import parser.ast.ASTNode;
import parser.utils.ParserException;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransIfTest {
    @Test
    public void test() throws LexicalException, ParserException {
        String source = "if(a){" +
                "b = 1" +
                "}";
        ASTNode ast = Parser.parse(source);
        Translator translator = new Translator();
        TAProgram program = translator.translate(ast);

//        System.out.println(program.toString());

        String expect = "IF a ELSE L0\n" +
                "SP -1\n" +
                "b = 1\n" +
                "SP 1\n" +
                "L0:";

        assertEquals(program.toString(), expect);
    }

    @Test
    public void testIfElse() throws LexicalException, ParserException {
        String source = "if(a){\n" +
                "b = 1\n" +
                "} else {\n" +
                "b = 2\n" +
                "}\n";
        ASTNode ast = Parser.parse(source);
        Translator translator = new Translator();
        TAProgram program = translator.translate(ast);

//        System.out.println(program.toString());

        String expect = "IF a ELSE L0\n" +
                "SP -1\n" +
                "b = 1\n" +
                "SP 1\n" +
                "GOTO L1\n" +
                "L0:\n" +
                "SP -1\n" +
                "b = 2\n" +
                "SP 1\n" +
                "L1:";

        assertEquals(program.toString(), expect);
    }

    @Test
    public void testComplexIf() throws LexicalException, ParserException, FileNotFoundException, UnsupportedEncodingException {
        ASTNode ast = Parser.fromFile("./example/complex-if.ms");
        Translator translator = new Translator();
        TAProgram program = translator.translate(ast);
        System.out.println(program.toString());

        String expect = "p0 = a == 1\n" +
                "IF p0 ELSE L0\n" +
                "SP -2\n" +
                "b = 100\n" +
                "SP 2\n" +
                "GOTO L5\n" +
                "L0:\n" +
                "p1 = a == 2\n" +
                "IF p1 ELSE L1\n" +
                "SP -3\n" +
                "b = 500\n" +
                "SP 3\n" +
                "GOTO L4\n" +
                "L1:\n" +
                "p2 = a == 3\n" +
                "IF p2 ELSE L2\n" +
                "SP -4\n" +
                "p1 = a * 3\n" +
                "b = p1\n" +
                "SP 4\n" +
                "GOTO L3\n" +
                "L2:\n" +
                "SP -4\n" +
                "b = -1\n" +
                "SP 4\n" +
                "L3:\n" +
                "L4:\n" +
                "L5:";

        assertEquals(program.toString(), expect);
    }
}
