package parser.ast;

import parser.utils.ParserException;
import parser.utils.PeekTokenIterator;

public class Program extends Block {
    public Program() {
        super();
    }

    public static ASTNode parse(PeekTokenIterator it) throws ParserException {
        ASTNode program = new Program();
        ASTNode stmt;
        while((stmt = Stmt.parseStmt(it)) != null) {
            program.addChild(stmt);
        }
        return program;
    }
}
