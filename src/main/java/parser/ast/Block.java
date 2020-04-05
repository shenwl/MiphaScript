package parser.ast;

import parser.utils.ParserException;
import parser.utils.PeekTokenIterator;

public class Block extends Stmt {
    public Block(ASTNode parent) {
        super(parent, ASTNodeTypes.BLOCK, "block");
    }

    public static ASTNode parse(ASTNode parent, PeekTokenIterator it) throws ParserException {
        it.nextMatch("{");
        ASTNode block = new Block(parent);
        ASTNode stmt;
        while((stmt = Stmt.parseStmt(parent, it)) != null) {
            block.addChild(stmt);
        }
        it.nextMatch("}");
        return block;
    }
}
