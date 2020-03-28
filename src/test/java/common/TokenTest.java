package common;

import lexer.Token;
import lexer.TokenType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenTest {
    void assertToken(Token token, String value, TokenType type) {
        assertEquals(type, token.getType());
        assertEquals(value, token.getValue());
    }

    @Test
    public void test_varOrKeyword() {
        PeekIterator<Character> it1 = new PeekIterator<>("if abc".chars().mapToObj(c -> (char)c));
        PeekIterator<Character> it2 = new PeekIterator<>("true abc".chars().mapToObj(c -> (char)c));

        Token token1 = Token.makeVarOrKeyword(it1);
        Token token2 = Token.makeVarOrKeyword(it2);

        assertToken(token1, "if", TokenType.KEYWORD);
        assertToken(token2, "true", TokenType.BOOLEAN);

        it1.next();

        Token token3 = Token.makeVarOrKeyword(it1);
        assertToken(token3, "abc", TokenType.VARIABLE);
    }
}
