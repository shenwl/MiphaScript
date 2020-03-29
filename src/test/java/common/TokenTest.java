package common;

import exceptions.LexicalException;
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
        PeekIterator<Character> it1 = new PeekIterator<>("if abc".chars().mapToObj(c -> (char) c));
        PeekIterator<Character> it2 = new PeekIterator<>("true abc".chars().mapToObj(c -> (char) c));

        Token token1 = Token.makeVarOrKeyword(it1);
        Token token2 = Token.makeVarOrKeyword(it2);

        assertToken(token1, "if", TokenType.KEYWORD);
        assertToken(token2, "true", TokenType.BOOLEAN);

        it1.next();

        Token token3 = Token.makeVarOrKeyword(it1);
        assertToken(token3, "abc", TokenType.VARIABLE);
    }

    @Test
    public void test_makeString() throws LexicalException {
        String[] tests = {
                "\"123\"",
                "\'123\'"
        };
        for (String test : tests) {
            PeekIterator<Character> it = new PeekIterator<>(test.chars().mapToObj(c -> (char) c));
            Token token = Token.makeString(it);
            assertToken(token, test, TokenType.STRING);
        }
    }

    @Test
    public void test_makeOperator() throws LexicalException {
        String[] tests = {
                "+ xxx",
                "++mmm",
                "/=g",
                "==1",
                "&111",
                "||xx",
                "^yxx",
                "%7",
                "%=777",
        };
        String[] results = {
                "+",
                "++",
                "/=",
                "==",
                "&",
                "||",
                "^",
                "%",
                "%="
        };
        int i = 0;
        for (String test : tests) {
            PeekIterator<Character> it = new PeekIterator<>(test.chars().mapToObj(c -> (char) c));
            Token token = Token.makeOperator(it);
            assertToken(token, results[i++], TokenType.OPERATOR);
        }
    }
}
