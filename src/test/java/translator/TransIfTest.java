package translator;

import exceptions.LexicalException;
import org.junit.jupiter.api.Test;
import parser.Parser;
import parser.ast.ASTNode;
import parser.utils.ParserException;

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

        System.out.println(program.toString());

        String expect = "IF a ELSE L0\n" +
                "SP -1\n" +
                "b = 1\n" +
                "SP 1\n" +
                "L0:";

        assertEquals(program.toString(), expect);
    }
}
