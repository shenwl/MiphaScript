package gen;

import exceptions.LexicalException;
import org.junit.jupiter.api.Test;
import parser.Parser;
import parser.ast.ASTNode;
import parser.utils.ParserException;
import translator.TAProgram;
import translator.Translator;
import translator.symbol.StaticSymbolTable;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GenTest {

    @Test
    public void exprEvaluate() throws LexicalException, ParserException {

        String source = "var a = 3*2*(5+1)";
        ASTNode astNode = Parser.parse(source);
        Translator translator = new Translator();
        TAProgram taProgram = translator.translate(astNode);

        String staticSymbolTableString = taProgram.getStaticSymbolTable().toString();

        assertEquals("0:3\n" +
                "1:2\n" +
                "2:5\n" +
                "3:1", staticSymbolTableString);
        OpCodeGen generator = new OpCodeGen();
        OpCodeProgram program = generator.gen(taProgram);

        System.out.println(program);

        assertEquals("# p0 = 5 + 1\n" +
                "LW S0 STATIC 2\n" +
                "LW S1 STATIC 3\n" +
                "ADD S2 S0 S1\n" +
                "SW S2 SP -1\n" +
                "# p1 = 2 * p0\n" +
                "LW S0 STATIC 1\n" +
                "LW S1 SP -1\n" +
                "MULT S0 S1\n" +
                "MFLO S2\n" +
                "SW S2 SP -2\n" +
                "# p2 = 3 * p1\n" +
                "LW S0 STATIC 0\n" +
                "LW S1 SP -2\n" +
                "MULT S0 S1\n" +
                "MFLO S2\n" +
                "SW S2 SP -3\n" +
                "# a = p2\n" +
                "LW S0 SP -3\n" +
                "SW S0 SP 0", program.toString());
    }
}
