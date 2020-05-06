package gen.operand;

public class ImmediateNumber extends Operand {
    private int value;

    public ImmediateNumber(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value + "";
    }
}
