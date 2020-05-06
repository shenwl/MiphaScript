package gen.operand;

public class Label extends Offset {
    String label;

    public Label(String label) {
        super(0);
        this.label = label;
    }

    public Label(int offset, String label) {
        super(offset);
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
