package gen;

import gen.operand.Offset;
import gen.operand.Operand;
import gen.operand.Register;
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

    public static Instruction jump(OpCode code, int offset) {
        Instruction i = new Instruction(code);
        i.opList.add(new Offset(offset));
        return i;
    }


    private static Instruction offsetInstruction(
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
}
