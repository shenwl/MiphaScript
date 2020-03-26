package common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PeekIteratorTest {
    @Test
    public void test_peek() {
        String source = "ashdkhasldhaksdhkajhdkjsadgkjsahdjkas";
        PeekIterator<Character> peekIterator = new PeekIterator<>(source.chars().mapToObj(c -> (char)c));

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
    public void test_lookahead2() {
        String source = "abcdef";
        PeekIterator<Character> peekIterator = new PeekIterator<>(source.chars().mapToObj(c -> (char)c));
        assertEquals('a', peekIterator.next());
        assertEquals('b', peekIterator.next());
        assertEquals('c', peekIterator.next());

        peekIterator.putBack();
        peekIterator.putBack();

        assertEquals('b', peekIterator.next());
        assertEquals('c', peekIterator.next());

    }
}
