package lexer;

import common.AlphabetHelper;
import common.PeekIterator;
import exceptions.LexicalException;

import java.util.ArrayList;
import java.util.stream.Stream;

public class Lexer {
    /**
     * 分析源码文本流，产生Token
     *
     * @param source 源码文本流
     * @return Token列表
     */
    public ArrayList<Token> analyse(Stream source) throws LexicalException {
        ArrayList<Token> tokens = new ArrayList<>();
        PeekIterator<Character> iterator = new PeekIterator<>(source, (char) 0);

        while (iterator.hasNext()) {
            char c = iterator.next();
            // 结束符
            if (c == 0) {
                break;
            }

            char lookahead = iterator.peek();

            // 无效字符
            if (c == ' ' || c == '\n') {
                continue;
            }
            // 删除注释
            if (c == '/') {
                if (lookahead == '/') {
                    while (iterator.hasNext()) {
                        c = iterator.next();
                        if (c == '\n') {
                            break;
                        }
                    }
                } else if (lookahead == '*') {
                    boolean valid = false; // 检查是否闭合
                    while (iterator.hasNext()) {
                        char p = iterator.next();
                        if (p == '*' && iterator.peek() == '/') {
                            iterator.next();
                            valid = true;
                            break;
                        }
                    }
                    if(!valid) {
                        throw new LexicalException("comments not match");
                    }
                }
                continue;
            }

            // 现在状态机没有涉及的符号
            if (c == '{' || c == '}' || c == '(' || c == ')') {
                tokens.add(new Token(TokenType.BRACKET, c + ""));
                continue;
            }
            // 需要状态机处理，需要把元素放回流，再把流转交给状态机
            // 字符串
            if (c == '"' || c == '\'') {
                iterator.putBack();
                tokens.add(Token.makeString(iterator));
                continue;
            }
            // 变量或者关键字
            if (AlphabetHelper.isLetter(c)) {
                iterator.putBack();
                tokens.add(Token.makeVarOrKeyword(iterator));
                continue;
            }
            // 数字，+-，.开头都可能是数字
            if (AlphabetHelper.isNumber(c)) {
                iterator.putBack();
                tokens.add(Token.makeNumber(iterator));
                continue;
            }
            if ((c == '+' || c == '-' || c == '.') && AlphabetHelper.isNumber(lookahead)) {
                Token lastToken = tokens.size() == 0 ? null : tokens.get(tokens.size() - 1);

                if (lastToken == null || !lastToken.isNumber() || lastToken.isOperator()) {
                    iterator.putBack();
                    tokens.add(Token.makeNumber(iterator));
                    continue;
                }
            }
            // 操作符
            if (AlphabetHelper.isOperator(c)) {
                iterator.putBack();
                tokens.add(Token.makeOperator(iterator));
                continue;
            }
            throw new LexicalException(c);
        }
        return tokens;
    }
}
