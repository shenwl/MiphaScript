package translator;

import exceptions.LexicalException;
import org.junit.jupiter.api.Test;
import parser.Parser;
import parser.ast.ASTNode;
import parser.utils.ParserException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransExprTest {
    void assertOpcodes(String[] lines, ArrayList<TAInstruction> opcodes) {
        for (int i = 0; i < opcodes.size(); i++) {
            TAInstruction opcode = opcodes.get(i);
            assertEquals(lines[i], opcode.toString());
        }
    }

    @Test
    public void transExpr() throws LexicalException, ParserException {
        String source = "a + (b - c) + d * (b - c) * 2";
        ASTNode astNode = Parser.parse(source);

        Translator translator = new Translator();
        TAProgram taProgram = translator.translate(astNode);
        String codes = taProgram.toString();

        String[] expectedResults = new String[] {
                "p0 = b - c",
                "p1 = b - c",
                "p2 = p1 * 2",
                "p3 = d * p2",
                "p4 = p0 + p3",
                "p5 = a + p4",
        };
    }
}
