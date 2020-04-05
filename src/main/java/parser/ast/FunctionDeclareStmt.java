package parser.ast;

import lexer.Token;
import lexer.TokenType;
import parser.utils.ParserException;
import parser.utils.PeekTokenIterator;

public class FunctionDeclareStmt extends Stmt {
    public FunctionDeclareStmt(ASTNode parent) {
        super(parent, ASTNodeTypes.FUNCTION_DECLARE_STMT, "function");
    }

    public static ASTNode parse(ASTNode parent, PeekTokenIterator it) throws ParserException {
        it.nextMatch("func");

        FunctionDeclareStmt func = new FunctionDeclareStmt(parent);
        Token lexeme = it.peek();
        // 解析函数名次
        Variable functionVariable = (Variable)Factor.parse(parent, it);

        func.setLexeme(lexeme);
        func.addChild(functionVariable);

        // 解析参数
        it.nextMatch("(");
        ASTNode args = FunctionArgs.parse(parent, it);
        it.nextMatch(") ");

        func.addChild(args);

        // 解析返回类型声明
        Token keyword = it.nextMatch(TokenType.KEYWORD);
        if(!keyword.isType()) {
            throw new ParserException(keyword);
        }
        functionVariable.setTypeLexeme(keyword);
        // 解析函数体
        ASTNode block = Block.parse(parent, it);
        func.addChild(block);
        return func;
    }

    public ASTNode getArgs() {
        return this.getChild(1);
    }

    public Variable getFunctionVariable() {
        return (Variable)this.getChild(0);
    }

    public String getFunctionType() {
        return this.getFunctionVariable().getTypeLexeme().getValue();
    }

    public Block getBlock() {
        return (Block)this.getChild(2);
    }
}
