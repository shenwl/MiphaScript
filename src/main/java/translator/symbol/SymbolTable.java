package translator.symbol;

import lexer.Token;
import lexer.TokenType;

import java.util.ArrayList;
import java.util.Optional;

public class SymbolTable {
    public SymbolTable parent = null;
    private ArrayList<SymbolTable> children;
    private ArrayList<Symbol> symbols;

    private int tempIndex = 0;      // 存储计算所需的临时变量
    private int offsetIndex = 0;
    private int level = 0;          // 当前节点的高度(在树结构中)

    public SymbolTable() {
        this.children = new ArrayList<>();
        this.symbols = new ArrayList<>();
    }

    public void addSymbol(Symbol symbol) {
        this.symbols.add(symbol);
        symbol.setParent(this);
    }

    public boolean exist(Token lexeme) {
        Optional<Symbol> _symbol = this.symbols.stream()
                .filter(item -> item.lexeme.getValue().equals(lexeme.getValue()))
                .findFirst();

        if (_symbol.isPresent()) {
            return true;
        }

        // 一层层递归查找
        if (this.parent != null) {
            return this.parent.exist(lexeme);
        }

        return false;
    }

    /**
     * 当前作用域使用的一个符号，找到它相对当前block定义的位置并clone
     *
     * @param lexeme
     * @param layoutOffset layoutOffset为1代表变量是本层级的，比1大代表往外多少层的
     * @return
     */
    public Symbol cloneFromSymbolTree(Token lexeme, int layoutOffset) {
        Optional<Symbol> _symbol = this.symbols.stream()
                .filter(item -> item.lexeme.getValue().equals(lexeme.getValue()))
                .findFirst();

        if (_symbol.isPresent()) {
            Symbol symbol = _symbol.get().copy();
            symbol.setLayerOffset(layoutOffset);
            return symbol;
        }

        // layoutOffset每往父级走一次就+1
        if (this.parent != null) {
            return this.parent.cloneFromSymbolTree(lexeme, layoutOffset + 1);
        }

        return null;
    }

    public Symbol createSymbolByLexeme(Token lexeme) {
        Symbol symbol = null;

        if (lexeme.isScalar()) {
            symbol = Symbol.createImmediateSymbol(lexeme);
        } else {
            symbol = cloneFromSymbolTree(lexeme, 0);
            if(symbol == null) {
                symbol = Symbol.createAddressSymbol(lexeme, this.offsetIndex++);
            }
        }

        return symbol;
    }

    public Symbol createVariable() {
        // var a = 1 + 2 * 3
        // p0 = 2 * 3
        Token lexeme = new Token(TokenType.VARIABLE, "p" + this.tempIndex++);
        Symbol symbol = Symbol.createAddressSymbol(lexeme, this.offsetIndex++);
        this.addSymbol(symbol);
        return symbol;
    }

    public void createLabel(String label, Token lexeme) {
        Symbol labelSymbol = Symbol.createLabelSymbol(lexeme, label);
        this.addSymbol(labelSymbol);
    }

    public void addChild(SymbolTable child) {
        child.parent = this;
        child.level = this.level + 1;
        this.children.add(child);
    }

    /**
     * 符号表里存了几个要占空间的变量
     * example:  { var a = 1 {{var b = a}} }
     * var b = a 这行的a是不占空间的
     * @return offsetIndex
     */
    public int localSize() {
        return this.offsetIndex;
    }

    public ArrayList<SymbolTable> getChildren() {
        return children;
    }

    public ArrayList<Symbol> getSymbols() {
        return symbols;
    }
}


