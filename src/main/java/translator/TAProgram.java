package translator;

import org.apache.commons.lang3.StringUtils;
import translator.symbol.StaticSymbolTable;
import translator.symbol.Symbol;
import translator.symbol.SymbolTable;
import translator.symbol.SymbolType;

import java.util.ArrayList;

/**
 * 三地址代码
 */
public class TAProgram {
    private ArrayList<TAInstruction> instructions = new ArrayList<>();
    // 在一个程序中有很多label，这里是为了分配数字
    private int labelCounter = 0;

    private StaticSymbolTable staticSymbolTable = new StaticSymbolTable();

    public StaticSymbolTable getStaticSymbolTable() {
        return staticSymbolTable;
    }

    public void add(TAInstruction code) {
        instructions.add(code);
    }

    public TAInstruction addLabel() {
        String label = "L" + labelCounter++;
        TAInstruction taCode = new TAInstruction(TAInstructionType.LABEL, null, null, null, null);
        taCode.setArg1(label);
        instructions.add(taCode);
        return taCode;
    }

    public ArrayList<TAInstruction> getInstructions() {
        return instructions;
    }

    public void setStaticSymbols(SymbolTable symbolTable) {
        for (Symbol symbol : symbolTable.getSymbols()) {
            if (symbol.getType() == SymbolType.IMMEDIATE_SYMBOL) {
                staticSymbolTable.add(symbol);
            }
        }

        for (SymbolTable child : symbolTable.getChildren()) {
            setStaticSymbols(child);
        }
    }

    @Override
    public String toString() {
        ArrayList<String> lines = new ArrayList<>();

        for (TAInstruction instruction : instructions) {
            lines.add(instruction.toString());
        }

        return StringUtils.join(lines, "\n");
    }
}
