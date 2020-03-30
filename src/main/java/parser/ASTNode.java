package parser;

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

    public ASTNode(ASTNode parent) {
        this.parent = parent;
    }

    public ASTNode(ASTNode parent,  ASTNodeTypes type, String label) {
        this.parent = parent;
        this.type = type;
        this.label = label;
    }

    public ASTNode getChild(int index) {
        return children.get(index);
    }

    public void addChild(ASTNode child) {
        children.add(child);
    }

    public Token getLexeme() {
        return lexeme;
    }

    public ArrayList<ASTNode> getChildren() {
        return children;
    }
}
