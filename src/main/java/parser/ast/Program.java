package parser.ast;

import parser.utils.ParserException;
import parser.utils.PeekTokenIterator;

public class Program extends Block {
    public Program(ASTNode parent) {
        super(parent);
    }

    public static ASTNode parse(ASTNode parent, PeekTokenIterator it) throws ParserException {
        ASTNode program = new Program(parent);
        ASTNode stmt;
        while((stmt = Stmt.parseStmt(parent, it)) != null) {
            program.addChild(stmt);
        }
        return program;
    }
}
