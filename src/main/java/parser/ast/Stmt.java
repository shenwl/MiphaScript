package parser.ast;

import lexer.Token;
import parser.utils.ParserException;
import parser.utils.PeekTokenIterator;

public abstract class Stmt extends ASTNode {
    public Stmt(ASTNodeTypes type, String label) {
        super(type, label);
    }

    public static ASTNode parseStmt(PeekTokenIterator it) throws ParserException {
        // 解析语句需要往前看两个
        Token token = it.next();
        Token lookahead = it.peek();
        it.putBack();


        if(token.isVariable() && lookahead != null && lookahead.getValue().equals("=")) {
            return AssignStmt.parse(it);
        }
        if(token.getValue().equals("var")) {
            return DeclareStmt.parse(it);
        }
        if(token.getValue().equals("func")) {
            return FunctionDeclareStmt.parse(it);
        }
        if(token.getValue().equals("return")) {
            return ReturnStmt.parse(it);
        }
        if(token.getValue().equals("if")) {
            return IfStmt.parse(it);
        }
        if(token.getValue().equals("{")) {
            return Block.parse(it);
        }
        return null;
    }
}
