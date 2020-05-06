package gen;

/**
 * 操作符
 */
public class OpCode {
    private String name;
    private byte value;
    private AddressingType addressingType;

    private static OpCode[] opCodes = new OpCode[63];

    public OpCode(AddressingType addressingType, String name, byte value) {
        this.name = name;
        this.value = value;
        this.addressingType = addressingType;
        opCodes[value] = this;
    }

    // 加、减、乘（及对应的立即寻址方法）
    public static final OpCode ADD = new OpCode(AddressingType.REGISTER, "ADD", (byte) 0x01);
    public static final OpCode SUB = new OpCode(AddressingType.REGISTER, "SUB", (byte) 0x02);
    public static final OpCode MULT = new OpCode(AddressingType.REGISTER, "MULT", (byte) 0x03);

    public static final OpCode ADDI = new OpCode(AddressingType.IMMEDIATE, "ADDI", (byte) 0x05);
    public static final OpCode SUBI = new OpCode(AddressingType.IMMEDIATE, "SUBI", (byte) 0x06);
    public static final OpCode MULTI = new OpCode(AddressingType.IMMEDIATE, "MULTI", (byte) 0x07);

    // 将LO寄存器的值存到想定义的寄存器
    public static final OpCode MFLO = new OpCode(AddressingType.REGISTER, "MFLO", (byte) 0x08);

    // 比较相等
    public static final OpCode EQ = new OpCode(AddressingType.REGISTER, "EQ", (byte) 0x09);
    // 比较不等
    public static final OpCode BNE = new OpCode(AddressingType.OFFSET, "BNE", (byte) 0x15);

    // 把寄存器的值存到内存
    public static final OpCode SW = new OpCode(AddressingType.OFFSET, "SW", (byte) 0x10);
    // 把内存的值导到寄存器
    public static final OpCode LW = new OpCode(AddressingType.OFFSET, "LW", (byte) 0x11);

    // 跳转
    public static final OpCode JUMP = new OpCode(AddressingType.JUMP, "JUMP", (byte) 0x20);
    // 函数跳转
    public static final OpCode JR = new OpCode(AddressingType.JUMP, "JR", (byte) 0x21);
    // 返回
    public static final OpCode RETURN = new OpCode(AddressingType.JUMP, "RETURN", (byte) 0x22);

    public static OpCode fromByte(byte byteCode) {
        return opCodes[byteCode];
    }

    public AddressingType getType() {
        return addressingType;
    }

    public byte getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name;
    }
}
