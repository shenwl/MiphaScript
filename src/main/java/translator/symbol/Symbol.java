package translator.symbol;

import lexer.Token;

public class Symbol {
    // union
    SymbolTable parent;
    Token lexeme;
    String label;

    SymbolType type;
    int offset;
    // 记录block的层级关系
    int layerOffset = 0;

    public Symbol(SymbolType type) {
        this.type = type;
    }

    public static Symbol createAddressSymbol(Token lexeme, int offset) {
        Symbol symbol = new Symbol(SymbolType.ADDRESS_SYMBOL);
        symbol.lexeme = lexeme;
        symbol.offset = offset;
        return symbol;
    }

    public static Symbol createImmediateSymbol(Token lexeme) {
        Symbol symbol = new Symbol(SymbolType.IMMEDIATE_SYMBOL);
        symbol.lexeme = lexeme;
        return symbol;
    }

    public static Symbol createLabelSymbol(Token lexeme, String label) {
        Symbol symbol = new Symbol(SymbolType.LABEL_SYMBOL);
        symbol.lexeme = lexeme;
        symbol.label = label;
        return symbol;
    }

    public Symbol copy() {
        Symbol symbol = new Symbol(this.type);
        symbol.lexeme = this.lexeme;
        symbol.label = this.label;
        symbol.offset = this.offset;
        symbol.layerOffset = this.layerOffset;
        symbol.type = this.type;
        return symbol;
    }

    public SymbolTable getParent() {
        return parent;
    }

    public void setParent(SymbolTable parent) {
        this.parent = parent;
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

    public void setLabel(String label) {
        this.label = label;
    }

    public SymbolType getType() {
        return type;
    }

    public void setType(SymbolType type) {
        this.type = type;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLayerOffset() {
        return layerOffset;
    }

    public void setLayerOffset(int layerOffset) {
        this.layerOffset = layerOffset;
    }

    @Override
    public String toString() {
        switch (type) {
            case ADDRESS_SYMBOL:
            case IMMEDIATE_SYMBOL:
                return lexeme.getValue();
            case LABEL_SYMBOL:
                return label;
        }
        return "";
    }
}
