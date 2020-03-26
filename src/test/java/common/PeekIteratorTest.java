package common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PeekIteratorTest {
    @Test
    public void test_peek() {
        String source = "ashdkhasldhaksdhkajhdkjsadgkjsahdjkas";
        PeekIterator<Character> peekIterator = new PeekIterator<>(source.chars().mapToObj(c -> (char) c));

        assertEquals('a', peekIterator.next());
        assertEquals('s', peekIterator.next());
        peekIterator.next();
        peekIterator.next();
        assertEquals('k', peekIterator.next());
        assertEquals('h', peekIterator.peek());
        assertEquals('h', peekIterator.peek());
        assertEquals('h', peekIterator.next());
        assertEquals('a', peekIterator.next());
    }

    @Test
    public void test_putBack() {
        String source = "abcdef";
        PeekIterator<Character> peekIterator = new PeekIterator<>(source.chars().mapToObj(c -> (char) c));
        assertEquals('a', peekIterator.next());
        assertEquals('b', peekIterator.next());
        assertEquals('c', peekIterator.next());

        peekIterator.putBack();
        peekIterator.putBack();

        assertEquals('b', peekIterator.next());
        assertEquals('c', peekIterator.next());

    }

    @Test
    public void test_endToken() {
        String source = "abcdef";
        PeekIterator<Character> peekIterator = new PeekIterator<>(source.chars().mapToObj(c -> (char) c), (char) 0);
        int i = 0;
        while (peekIterator.hasNext()) {
            if (i == 6) {
                assertEquals((char) 0, peekIterator.next());
            } else {
                assertEquals(source.charAt(i), peekIterator.next());
                i++;
            }
        }
    }
}
