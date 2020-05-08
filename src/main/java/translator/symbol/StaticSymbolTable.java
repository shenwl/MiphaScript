package translator.symbol;

import org.apache.commons.lang3.StringUtils;
import translator.TAInstruction;

import java.util.ArrayList;
import java.util.Hashtable;

public class StaticSymbolTable {
    private int offsetCounter = 0;

    private ArrayList<Symbol> symbols;
    private Hashtable<String, Symbol> offsetMap;

    public StaticSymbolTable() {
        this.symbols = new ArrayList<>();
        this.offsetMap = new Hashtable<>();
    }

    public void add(Symbol symbol) {
        String lexemeVal = symbol.getLexeme().getValue();
        if (!offsetMap.containsKey(lexemeVal)) {
            offsetMap.put(lexemeVal, symbol);
            symbol.setOffset(offsetCounter++);
            symbols.add(symbol);
            return;
        }
        Symbol samSymbol = offsetMap.get(lexemeVal);
        // Symbol的offset一致，则在内存中位置一致
        symbol.setOffset(samSymbol.offset);
    }

    public int size() {
        return this.symbols.size();
    }

    public ArrayList<Symbol> getSymbols() {
        return symbols;
    }

    @Override
    public String toString() {

        ArrayList<String> list = new ArrayList<>();
        for(int i = 0; i < this.symbols.size(); i++) {
            list.add(i+":" + this.symbols.get(i).toString());
        }
        return StringUtils.join(list, "\n");
    }
}
