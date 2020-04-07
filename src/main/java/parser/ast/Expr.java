package parser.ast;

import lexer.Token;
import parser.utils.ExprHOF;
import parser.utils.ParserException;
import parser.utils.PeekTokenIterator;
import parser.utils.PriorityTable;

public class Expr extends ASTNode {
    private static PriorityTable table = new PriorityTable();

    public Expr() {
        super();
    }

    public Expr(ASTNodeTypes type, Token lexeme) {
        super();
        this.type = type;
        this.label = lexeme.getValue();
        this.lexeme = lexeme;
    }

    // 左递归：E(k) -> E(k) op(k) E(k+1) | E(k+1)
    // 又递归：E(k) -> E(k+1) E_(k)  // Expr e = new Expr; e.left = E(k+1); e.op = op(k); e.right = E(k+1) E_(k)
    //       E_(k) -> op(k) E(k+1) E_(k) | ε
    private static ASTNode E(PeekTokenIterator it, int k) throws ParserException {
        if (k < table.size() - 1) {
            return combine(it, () -> E(it, k + 1), () -> E_(it, k));
        }
        // 最高优先级
        // E(t) -> F(factor) E_(k) | U(一元表达式) E_(k)
        // E_(t) -> op(t) E(t) E_(t) | ε
        return race(
                () -> combine(it, () -> U(it), () -> E_(it, k)),
                () -> combine(it, () -> F(it), () -> E_(it, k)),
                it
        );
    }

    // 解析因子
    private static ASTNode F(PeekTokenIterator it) {
        ASTNode factor = Factor.parse(it);

        if (factor == null) {
            return null;
        }
        return factor;
    }

    // 解析一元表达式
    private static ASTNode U(PeekTokenIterator it) throws ParserException {
        Token token = it.peek();
        String value = token.getValue();

        if (value.equals(("("))) {
            it.nextMatch("(");
            ASTNode expr = E(it, 0);
            it.nextMatch(")");
            return expr;
        }
        if (value.equals("++") || value.equals("--") || value.equals("!")) {
            Token t = it.peek();
            it.nextMatch(value);
            Expr unaryExpr = new Expr(ASTNodeTypes.UNARY_EXPR, t);
            unaryExpr.addChild(E(it, 0));
            return unaryExpr;
        }
        return null;
    }

    private static ASTNode E_(PeekTokenIterator it, int k) throws ParserException {
        Token token = it.peek();
        String value = token.getValue();

        if (table.get(k).indexOf(value) != -1) {
            Expr expr = new Expr(ASTNodeTypes.BINARY_EXPR, it.nextMatch(value));
            expr.addChild(combine(it, () -> E(it, k), () -> E_(it, k)));
            return expr;
        }
        return null;
    }

    private static ASTNode race(ExprHOF aFunc, ExprHOF bFunc, PeekTokenIterator it) throws ParserException {
        if (!it.hasNext()) {
            return null;
        }
        ASTNode a = aFunc.hoc();
        if (a != null) {
            return a;
        }
        return bFunc.hoc();
    }

    // E(k) -> E(k+1) E_(k)解决的逻辑
    // E(k+1)为null时返回E_(k)，E(k+1)不为nullE_(k)为null时，返回E(k+1)，都不为null时返回一个Expr
    private static ASTNode combine(PeekTokenIterator it, ExprHOF aFunc, ExprHOF bFunc) throws ParserException {
        ASTNode a = aFunc.hoc();
        if (a == null) {
            return it.hasNext() ? bFunc.hoc() : null;
        }
        ASTNode b = it.hasNext() ? bFunc.hoc() : null;
        if (b == null) {
            return a;
        }
        Expr expr = new Expr(ASTNodeTypes.BINARY_EXPR, b.lexeme);
        expr.addChild(a);
        expr.addChild(b.getChild(0));
        return expr;
    }

    public static ASTNode parse(PeekTokenIterator it) throws ParserException {
        return E(it, 0);
    }
}
