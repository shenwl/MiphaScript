package parser.ast;

import parser.utils.ParserException;
import parser.utils.PeekTokenIterator;

public class Block extends Stmt {
    public Block() {
        super(ASTNodeTypes.BLOCK, "block");
    }

    public static ASTNode parse(PeekTokenIterator it) throws ParserException {
        it.nextMatch("{");
        ASTNode block = new Block();
        ASTNode stmt;
        while((stmt = Stmt.parseStmt(it)) != null) {
            block.addChild(stmt);
        }
        it.nextMatch("}");
        return block;
    }
}
