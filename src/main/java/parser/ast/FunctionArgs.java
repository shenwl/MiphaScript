package parser.ast;

import lexer.Token;
import parser.utils.ParserException;
import parser.utils.PeekTokenIterator;

public class FunctionArgs extends ASTNode {
    public FunctionArgs(ASTNode parent) {
        super(parent);
        this.label = "args";
    }

    public static ASTNode parse(ASTNode parent, PeekTokenIterator it) throws ParserException {
        ASTNode args = new FunctionArgs(parent);

        while(it.peek().isType()){
            Token type = it.next();
            Variable variable = (Variable)Factor.parse(parent, it);
            variable.setTypeLexeme(type);
            args.addChild(variable);

            if(!it.peek().getValue().equals(")")) {
                it.nextMatch(",");
            }
        }
        return args;
    }
}
