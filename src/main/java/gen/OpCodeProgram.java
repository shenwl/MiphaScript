package gen;

import org.apache.commons.lang3.StringUtils;
import translator.TAProgram;
import translator.symbol.Symbol;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * 指令程序
 */
public class OpCodeProgram {
    /**
     * 入口
     */
    Integer entry = null;

    /**
     * 指令
     */
    ArrayList<Instruction> instructions = new ArrayList<>();

    /**
     * 注释
     *
     * @description 行号(instructions里的序号): 注释内容
     */
    Hashtable<Integer, String> comments = new Hashtable<>();

    public void add(Instruction i) {
        instructions.add(i);
    }

    public void addComment(String comment) {
        this.comments.put(this.instructions.size(), comment);
    }

    @Override
    public String toString() {
        ArrayList<String> parts = new ArrayList<>();
        for (int i = 0; i < instructions.size(); i++) {
            if (comments.containsKey(i)) {
                parts.add("# " + comments.get(i));
            }
            String str = instructions.get(i).toString();
            if(entry != null && i == entry) {
                str = "MAIM: " + str;
            }
            parts.add(str);
        }
        return StringUtils.join(parts, "\n");
    }

    public ArrayList<Integer> toByteCode() {
        ArrayList<Integer> codes = new ArrayList<>();
        for(Instruction instruction : instructions) {
            codes.add(instruction.toByteCode());
        }
        return codes;
    }

    public Integer getEntry() {
        return entry;
    }

    public void setEntry(Integer entry) {
        this.entry = entry;
    }


    /**
     * 读取三地址程序的静态符号表，转化为int
     * @param taProgram
     * @return
     */
    public ArrayList<Integer> getStaticArea(TAProgram taProgram) {
        ArrayList<Integer> list = new ArrayList<>();
        for(Symbol symbol : taProgram.getStaticSymbolTable().getSymbols()) {
            list.add(Integer.parseInt(symbol.getLexeme().getValue()));
        }
        return list;
    }
}
