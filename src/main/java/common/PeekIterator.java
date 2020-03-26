package common;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Stream;

public class PeekIterator<T> implements Iterator<T> {
    private final static int CACHE_SIZE = 10;
    private Iterator<T> iterator;
    private LinkedList<T> queueCache;
    private LinkedList<T> stackPutBack;

    private T _endToken = null;

    public PeekIterator(Stream<T> stream) {
        iterator = stream.iterator();
        queueCache = new LinkedList<>();
        stackPutBack = new LinkedList<>();
    }

    public PeekIterator(Stream<T> stream, T endToken) {
        _endToken = endToken;
        iterator = stream.iterator();
        queueCache = new LinkedList<>();
        stackPutBack = new LinkedList<>();
    }

    public T peek() {
        // 如果putBack栈中有指，就拿第一个
        if (stackPutBack.size() > 0) {
            return this.stackPutBack.getFirst();
        }
        if (!iterator.hasNext()) {
            return _endToken;
        }
        T val = next();
        this.putBack();
        return val;
    }

    /**
     * 将缓存数据放回流
     * 缓存：A -> B -> C -> D
     * 放回：D -> C -> B -> A
     */
    public void putBack() {
        if (queueCache.size() > 0) {
            // 把缓存的最后一项拿出来，放入stackPutBack；
            stackPutBack.push(queueCache.pollLast());
        }
    }

    @Override
    public boolean hasNext() {
        // putBack栈里有元素或者流中有元素
        return _endToken != null || stackPutBack.size() > 0 || iterator.hasNext();
    }

    @Override
    public T next() {
        T val = null;
        // 优先拿stackPutBack里的数据，没有了再去流里拿
        if (stackPutBack.size() > 0) {
            val = stackPutBack.pop();
        } else {
            if(!iterator.hasNext()) {
                T temp =  _endToken;
                _endToken = null;
                return temp;
            }
            val = iterator.next();
        }
        // 当缓存大于最大缓存 - 1时，把链表第一个元素（最先入队缓存的）移除，把位置留给next要拿出的元素
        while (queueCache.size() > CACHE_SIZE - 1) {
            queueCache.poll();
        }
        queueCache.add(val);
        return val;
    }
}
