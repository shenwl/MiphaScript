package gen.operand;

public class Offset extends Operand {
    int offset;

    public Offset(int offset) {
        super();
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getEncodedOffset() {
        // |--code--|--offset--|
        // |--0x01--|
        if (offset > 0) return offset;

        // 0x400 = 0b100 0000 0000
        return 0x400 | -offset;
    }

    public static Offset decodeOffset(int offset) {
        // 如果是负数
        if ((offset & 0x400) > 0) {
            // 0x3ff = 0b011 1111 1111
            offset = offset & 0x3ff;
            offset = -offset;
        }
        return new Offset(offset);
    }

    @Override
    public String toString() {
        return offset + "";
    }
}
