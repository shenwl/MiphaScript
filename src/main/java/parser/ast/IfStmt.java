package parser.ast;

import lexer.Token;
import parser.utils.ParserException;
import parser.utils.PeekTokenIterator;

public class IfStmt extends Stmt {
    public IfStmt(ASTNode parent) {
        super(parent, ASTNodeTypes.IF_STMT, "if");
    }

    // IfStmt -> If(Expr) Block Tail
    public static ASTNode parse(ASTNode parent, PeekTokenIterator it) throws ParserException {
        return parseIf(parent, it);
    }

    public static ASTNode parseIf(ASTNode parent, PeekTokenIterator it) throws ParserException {
        Token lexeme = it.nextMatch("if");
        it.nextMatch("(");

        ASTNode ifStmt = new IfStmt(parent);
        ASTNode expr = Expr.parse(parent, it);
        ifStmt.addChild(expr);

        it.nextMatch(")");

        ASTNode block = Block.parse(parent, it);
        ifStmt.addChild(block);

        ASTNode tail = parseTail(parent, it);
        if (tail != null) {
            ifStmt.addChild(tail);
        }
        return ifStmt;
    }

    public static ASTNode parseTail(ASTNode parent, PeekTokenIterator it) throws ParserException {
        if (!it.hasNext() || !it.peek().getValue().equals("else")) {
            return null;
        }
        it.nextMatch("else");
        Token lookahead = it.peek();

        if (lookahead.getValue().equals("{")) {
            return Block.parse(parent, it);
        } else if (lookahead.getValue().equals("if")) {
            return parseIf(parent, it);
        } else {
            return null;
        }
    }
}
