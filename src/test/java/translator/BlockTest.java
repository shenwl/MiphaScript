package translator;

import exceptions.LexicalException;
import org.junit.jupiter.api.Test;
import parser.Parser;
import parser.ast.ASTNode;
import parser.utils.ParserException;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BlockTest {
    @Test
    public void test() throws LexicalException, ParserException {
        String source = "var a = 1\n" +
                "{\n" +
                "var b = a * 100\n" +
                "}\n" +
                "{\n" +
                "var b = a * 100\n" +
                "}\n";
        ASTNode ast = Parser.parse(source);

        Translator translator = new Translator();

        TAProgram program = translator.translate(ast);

        System.out.println(program.toString());

        assertEquals(program.toString(), "a = 1\n" +
                "SP -1\n" +
                "p1 = a * 100\n" +
                "b = p1\n" +
                "SP 1\n" +
                "SP -1\n" +
                "p1 = a * 100\n" +
                "b = p1\n" +
                "SP 1");
    }
}
