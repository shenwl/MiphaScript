package translator;

import translator.symbol.Symbol;

/**
 * 三地址指令
 * https://www.yuque.com/shenwl/nr0tg4/kos1tx/edit
 */
public class TAInstruction {
    private Object arg1;
    private Object arg2;

    private String op;
    private Symbol result;
    private TAInstructionType type;

    public TAInstruction(TAInstructionType type, Symbol result, String op, Object arg1, Object arg2) {
        this.type = type;
        this.result = result;
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public Object getArg1() {
        return arg1;
    }

    public void setArg1(Object arg1) {
        this.arg1 = arg1;
    }

    public Object getArg2() {
        return arg2;
    }

    public void setArg2(Object arg2) {
        this.arg2 = arg2;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public Symbol getResult() {
        return result;
    }

    public void setResult(Symbol result) {
        this.result = result;
    }

    public TAInstructionType getType() {
        return type;
    }

    public void setType(TAInstructionType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        switch (type) {
            case ASSIGN:
                if (arg2 != null) {
                    return String.format("%s = %s %s %s", result, arg1, op, arg2);
                }
                return String.format("%s = %s", result, arg1);
            case IF:
                return String.format("IF %s ELSE %s", arg1, arg2);
            case GOTO:
                return String.format("GOTO %s", arg1);
            case LABEL:
                return arg1 + ":";
            case RETURN:
                // 此时arg1为Symbol，Symbol有toString
                return "RETURN " + arg1;
            case PARAM:
                return "PARAM " + arg1 + " " + arg2;
            case SP:
                return "SP " + arg1;
            case CALL:
                // 此时arg1为Label类型Symbol
                return "CALL " + arg1;

        }
        return "";
    }
}
