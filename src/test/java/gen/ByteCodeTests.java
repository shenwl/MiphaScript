package gen;

import exceptions.GeneratorException;
import gen.operand.*;
import org.junit.jupiter.api.Test;
import translator.symbol.Symbol;
import translator.symbol.SymbolType;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ByteCodeTests {
    @Test
    public void add() throws GeneratorException {
        Instruction a = new Instruction(OpCode.ADD);
        a.opList.add(Register.S2);
        a.opList.add(Register.S0);
        a.opList.add(Register.S1);
        assertSameInstruction(a, Instruction.fromByteCode(a.toByteCode()));
    }

    void assertSameInstruction(Instruction a, Instruction b) throws GeneratorException {
        assertEquals(a.getOpCode(), b.getOpCode());
        assertEquals(a.opList.size(), b.opList.size());
        for (int i = 0; i < a.opList.size(); i++) {
            Operand p = a.opList.get(i);
            Operand q = b.opList.get(i);
            if (p.getClass() == Label.class) {
                assertEquals(q.getClass(), Offset.class);
            } else {
                assertEquals(p.getClass(), q.getClass());
            }
            if (p.getClass() == ImmediateNumber.class) {
                assertEquals(((ImmediateNumber) p).getValue(), ((ImmediateNumber) q).getValue());
            } else if (p.getClass() == Offset.class) {
                assertEquals(((Offset) p).getOffset(), ((Offset) q).getOffset());
            } else if (p.getClass() == Register.class) {
                assertEquals(((Register) p).getAddr(), ((Register) q).getAddr());
            } else if (p.getClass() == Label.class) {
                assertEquals(((Label) p).getOffset(), ((Offset) q).getOffset());
            } else {
                throw new GeneratorException("Unsupported encode/decode type:" + p.getClass());
            }
        }
    }

    @Test
    public void mult() throws GeneratorException {
        Instruction a = new Instruction(OpCode.MULT);
        a.opList.add(Register.S0);
        a.opList.add(Register.S1);

        int byteCode = a.toByteCode();
        Instruction b = Instruction.fromByteCode(byteCode);
        assertSameInstruction(a, b);
    }


    @Test
    public void jump() throws GeneratorException {
        Instruction a = new Instruction(OpCode.JUMP);
        Label label = new Label("L0");
        a.opList.add(label);
        label.setOffset(-100);
        assertSameInstruction(a, Instruction.fromByteCode(a.toByteCode()));
    }

    @Test
    public void jr() throws GeneratorException {
        Instruction a = new Instruction(OpCode.JR);
        Label label = new Label("L0");
        a.opList.add(label);
        label.setOffset(100);
        assertSameInstruction(a, Instruction.fromByteCode(a.toByteCode()));
    }

    @Test
    public void sw() throws GeneratorException {
        Symbol symbol = new Symbol(SymbolType.ADDRESS_SYMBOL);
        symbol.setOffset(-100);
        Instruction a = Instruction.saveToMemory(Register.S0, symbol);

        assertSameInstruction(a, Instruction.fromByteCode(a.toByteCode()));
    }


    @Test
    public void sw1() throws GeneratorException {
        Symbol symbol = new Symbol(SymbolType.IMMEDIATE_SYMBOL);
        symbol.setOffset(-100);
        Instruction a = Instruction.saveToMemory(Register.S0, symbol);

        assertSameInstruction(a, Instruction.fromByteCode(a.toByteCode()));
    }

    @Test
    public void lw() throws GeneratorException {
        Symbol symbol = new Symbol(SymbolType.ADDRESS_SYMBOL);
        symbol.setOffset(100);
        Instruction a = Instruction.loadToRegister(Register.S0, symbol);

        assertSameInstruction(a, Instruction.fromByteCode(a.toByteCode()));
    }


    @Test
    public void lw1() throws GeneratorException {
        Symbol symbol = new Symbol(SymbolType.IMMEDIATE_SYMBOL);
        symbol.setOffset(100);
        Instruction a = Instruction.loadToRegister(Register.S0, symbol);

        assertSameInstruction(a, Instruction.fromByteCode(a.toByteCode()));
    }

    @Test
    public void sp() throws GeneratorException {
        Instruction a = Instruction.immediate(OpCode.ADDI, Register.SP, new ImmediateNumber(100));
        assertSameInstruction(a, Instruction.fromByteCode(a.toByteCode()));
    }

    @Test
    public void bne() throws GeneratorException {
        Instruction a = Instruction.bne(Register.S0, Register.S1, "L0");
        ((Label) a.opList.get(2)).setOffset(100);

        assertSameInstruction(a, Instruction.fromByteCode(a.toByteCode()));
    }

}
