package common;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlphabetHelperTest {
    @Test
    public void test() {
        assertEquals(true, AlphabetHelper.isLetter('a'));
        assertEquals(false, AlphabetHelper.isLetter('*'));

        assertEquals(true, AlphabetHelper.isNumber('1'));
        assertEquals(true, AlphabetHelper.isNumber('0'));
        assertEquals(true, AlphabetHelper.isNumber('9'));

        assertEquals(true, AlphabetHelper.isOperator('*'));
        assertEquals(true, AlphabetHelper.isOperator('+'));
        assertEquals(true, AlphabetHelper.isOperator('-'));
        assertEquals(true, AlphabetHelper.isOperator('&'));
        assertEquals(true, AlphabetHelper.isOperator('/'));
        assertEquals(true, AlphabetHelper.isOperator('^'));
        assertEquals(true, AlphabetHelper.isOperator('='));
        assertEquals(false, AlphabetHelper.isOperator('A'));


        assertEquals(true, AlphabetHelper.isLiteral('a'));
        assertEquals(true, AlphabetHelper.isLiteral('B'));
        assertEquals(true, AlphabetHelper.isLiteral('_'));
        assertEquals(false, AlphabetHelper.isLiteral('*'));
    }
}
