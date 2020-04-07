package parser.ast;

import lexer.Token;

import java.util.ArrayList;

public abstract class ASTNode {
    /* 树 */
    protected ArrayList<ASTNode> children = new ArrayList<>();
    protected ASTNode parent;

    /* 关键信息 */
    protected Token lexeme;         // 词法单元
    protected String label;         // 标签（备注）
    protected ASTNodeTypes type;    // 类型

    // 类型
    protected Token typeLexeme;

    public ASTNode() {
        this.parent = null;
    }

    public ASTNode(ASTNodeTypes type, String label) {
        this.parent = null;
        this.type = type;
        this.label = label;
    }

    public ASTNode getChild(int index) {
        return children.get(index);
    }

    public void addChild(ASTNode child) {
        child.parent= this;
        children.add(child);
    }

    public Token getLexeme() {
        return lexeme;
    }

    public void setLexeme(Token lexeme) {
        this.lexeme = lexeme;
    }

    public String getLabel() {
        return label;
    }

    public ASTNodeTypes getType() {
        return type;
    }

    public void setType(ASTNodeTypes type) {
        this.type = type;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ArrayList<ASTNode> getChildren() {
        return children;
    }

    public Token getTypeLexeme() {
        return typeLexeme;
    }

    public void setTypeLexeme(Token typeLexeme) {
        this.typeLexeme = typeLexeme;
    }
}
