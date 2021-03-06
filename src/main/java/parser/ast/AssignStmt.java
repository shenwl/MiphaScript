package parser.ast;

import lexer.Token;
import parser.utils.ParserException;
import parser.utils.PeekTokenIterator;

public class AssignStmt extends Stmt {
    public AssignStmt() {
        super(ASTNodeTypes.ASSIGN_STMT, "assign");
    }

    // 逻辑同步Declare.parse，但是没有"var"
    public static ASTNode parse(PeekTokenIterator it) throws ParserException {
        ASTNode stmt = new AssignStmt();
        // 匹配（变量）factor
        Token token = it.peek();
        ASTNode factor = Factor.parse(it);
        if (factor == null) {
            throw new ParserException(token);
        }
        stmt.addChild(factor);
        // 匹配 "=" 后面的表达式
        Token lexeme = it.nextMatch("=");
        ASTNode expr = Expr.parse(it);
        stmt.addChild(expr);
        stmt.setLexeme(lexeme);
        return stmt;
    }
}
