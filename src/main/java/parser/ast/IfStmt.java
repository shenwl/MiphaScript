package parser.ast;

import lexer.Token;
import parser.utils.ParserException;
import parser.utils.PeekTokenIterator;

public class IfStmt extends Stmt {
    public IfStmt() {
        super(ASTNodeTypes.IF_STMT, "if");
    }

    // IfStmt -> If(Expr) Block Tail
    public static ASTNode parse(PeekTokenIterator it) throws ParserException {
        return parseIf(it);
    }

    public static ASTNode parseIf(PeekTokenIterator it) throws ParserException {
        Token lexeme = it.nextMatch("if");
        it.nextMatch("(");

        ASTNode ifStmt = new IfStmt();
        ASTNode expr = Expr.parse(it);
        ifStmt.addChild(expr);

        it.nextMatch(")");

        ASTNode block = Block.parse(it);
        ifStmt.addChild(block);

        ASTNode tail = parseTail(it);
        if (tail != null) {
            ifStmt.addChild(tail);
        }
        return ifStmt;
    }

    public static ASTNode parseTail(PeekTokenIterator it) throws ParserException {
        if (!it.hasNext() || !it.peek().getValue().equals("else")) {
            return null;
        }
        it.nextMatch("else");
        Token lookahead = it.peek();

        if (lookahead.getValue().equals("{")) {
            return Block.parse(it);
        } else if (lookahead.getValue().equals("if")) {
            return parseIf(it);
        } else {
            return null;
        }
    }

    public ASTNode getExpr() {
        return getChild(0);
    }

    public Block getBlock() {
        return (Block) getChild(1);
    }

    public Block getElseBlock() {
        ASTNode block = getChild(2);
        if (block instanceof Block) {
            return (Block) block;
        }
        return null;
    }

    public IfStmt getElseIfStmt() {
        ASTNode ifStmt = getChild(2);
        if (ifStmt instanceof IfStmt) {
            return (IfStmt) ifStmt;
        }
        return null;
    }
}
