package gen.operand;

import exceptions.GeneratorException;

/**
 * 寄存器
 */
public class Register extends Operand {
    private byte addr;
    private String name;

    private static Register[] registers = new Register[31];

    public Register(String name, byte addr) {
        this.addr = addr;
        this.name = name;
        registers[addr] = this;
    }

    // 寄存器列表
    public static final Register ZERO = new Register("ZERO", (byte) 1);
    public static final Register PC = new Register("PC", (byte) 2);
    public static final Register SP = new Register("SP", (byte) 3);
    public static final Register STATIC = new Register("STATIC", (byte) 4);
    public static final Register RA = new Register("RA", (byte) 5);

    public static final Register S0 = new Register("S0", (byte) 10);
    public static final Register S1 = new Register("S1", (byte) 11);
    public static final Register S2 = new Register("S2", (byte) 12);


    public static final Register LO = new Register("LO", (byte) 20);

    public static Register fromAddr(long addr) throws GeneratorException {
        if (addr < 0 || addr >= registers.length) {
            throw new GeneratorException("No Register at: " + addr);
        }
        if (registers[(int) addr] == null) {
            throw new GeneratorException("No Register at: " + addr);
        }
        return registers[(int) addr];
    }

    @Override
    public String toString() {
        return name;
    }

    public byte getAddr() {
        return addr;
    }
}
