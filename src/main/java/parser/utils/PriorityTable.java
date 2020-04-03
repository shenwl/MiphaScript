package parser.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PriorityTable {
    private List<List<String>> table = new ArrayList<>();

    public PriorityTable() {
        // 第一优先级 - 最后优先级
        table.add(Arrays.asList("&", "|", "^"));
        table.add(Arrays.asList("==", "!=", ">", "<", ">=", "<="));
        table.add(Arrays.asList("+", "-"));
        table.add(Arrays.asList("*", "/"));
        table.add(Arrays.asList("<<", ">>"));
    }

    public int size() {
        return table.size();
    }

    public List<String> get(int level) {
        return table.get(level);
    }
}
