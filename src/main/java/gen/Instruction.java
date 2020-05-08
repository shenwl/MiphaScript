package gen;

import gen.operand.*;
import org.apache.commons.lang3.NotImplementedException;
import translator.symbol.Symbol;
import translator.symbol.SymbolType;

import java.util.ArrayList;

public class Instruction {
    private static final int MASK_OPCODE = 0xfc000000;

    // 操作码类型
    private OpCode code;

    ArrayList<Operand> opList = new ArrayList<>();

    public Instruction(OpCode code) {
        this.code = code;
    }

    public OpCode getOpCode() {
        return code;
    }

    public static Instruction jump(OpCode code, int offset) {
        Instruction i = new Instruction(code);
        i.opList.add(new Offset(offset));
        return i;
    }

    public static Instruction offsetInstruction(
            OpCode opCode,
            Register r1,
            Register r2,
            Offset offset
    ) {
        Instruction i = new Instruction(opCode);

        i.opList.add(r1);
        i.opList.add(r2);
        i.opList.add(offset);
        return i;
    }

    public static Instruction loadToRegister(Register target, Symbol arg) {
        // 转成整数，目前程序只支持整数
        if (arg.getType() == SymbolType.ADDRESS_SYMBOL) {
            // 将寄存器SP指向的内存地址，带上offset，导入到target
            return offsetInstruction(OpCode.LW, target, Register.SP, new Offset(-arg.getOffset()));
        }
        if (arg.getType() == SymbolType.IMMEDIATE_SYMBOL) {
            return offsetInstruction(OpCode.LW, target, Register.STATIC, new Offset(arg.getOffset()));
        }
        throw new NotImplementedException("Cannot load type " + arg.getType() + " symbol to register");
    }

    public static Instruction saveToMemory(Register source, Symbol arg) {
        return offsetInstruction(OpCode.SW, source, Register.SP, new Offset(-arg.getOffset()));
    }

    public static Instruction register(OpCode opCode, Register a, Register b, Register c) {
        Instruction i = new Instruction(opCode);
        i.opList.add(a);
        if (b != null) {
            i.opList.add(b);
        }
        if (c != null) {
            i.opList.add(c);
        }
        return i;
    }

    public static Instruction immediate(OpCode opCode, Register r, ImmediateNumber number) {
        Instruction i = new Instruction(opCode);
        i.opList.add(r);
        i.opList.add(number);
        return i;
    }

    public static Instruction bne(Register a, Register b, String label) {
        Instruction i = new Instruction(OpCode.BNE);
        i.opList.add(a);
        i.opList.add(b);
        i.opList.add(new Label(label));
        return i;
    }

    /**
     * TODO：包含太多位运算，逻辑不太清晰
     */
    public Integer toByteCode() {
        int code = 0;
        int x = this.code.getValue();
        code |= x << 26;
        switch (this.code.getType()) {
            case IMMEDIATE: {
                Register r0 = (Register) this.opList.get(0);

                code |= r0.getAddr() << 21;
                code |= ((ImmediateNumber) this.opList.get(1)).getValue();
                return code;
            }
            case REGISTER: {
                Register r1 = (Register) this.opList.get(0);
                code |= r1.getAddr() << 21;
                if (this.opList.size() > 1) {
                    code |= ((Register) this.opList.get(1)).getAddr() << 16;
                    if (this.opList.size() > 2) {
                        int r2 = ((Register) this.opList.get(2)).getAddr();
                        code |= r2 << 11;
                    }
                }
                break;
            }
            case JUMP:
                if (this.opList.size() > 0) {
                    code |= ((Offset) this.opList.get(0)).getEncodedOffset();
                }
                break;
            case OFFSET:
                Register r1 = (Register) this.opList.get(0);
                Register r2 = (Register) this.opList.get(1);
                Offset offset = (Offset) this.opList.get(2);
                code |= r1.getAddr() << 21;
                code |= r2.getAddr() << 16;
                code |= offset.getEncodedOffset();
                break;
        }
        return code;
    }
}
