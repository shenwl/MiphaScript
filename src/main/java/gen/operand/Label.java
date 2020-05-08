package gen.operand;

public class Label extends Offset {
    String label;

    /**
     * label初始化没有offset，标签重定位之后才有
     */
    public Label(String label) {
        super(0);
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
