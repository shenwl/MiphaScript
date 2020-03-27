package exceptions;

public class LexicalException extends Exception {
    private String msg;

    public LexicalException(String msg) {
        this.msg = msg;
    }
    public LexicalException(char c) {
        this.msg = String.format("Unexpected character %c", c);
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
