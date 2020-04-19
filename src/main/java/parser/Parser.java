package parser;
import common.PeekIterator;
import exceptions.LexicalException;
import lexer.Lexer;
import lexer.Token;
import parser.ast.ASTNode;
import parser.ast.Program;
import parser.utils.ParserException;
import parser.utils.PeekTokenIterator;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Parser {
    public static ASTNode parse(String source) throws LexicalException, ParserException {
        Lexer lexer = new Lexer();
        ArrayList<Token> tokens = lexer.analyse(new PeekIterator<>(source.chars().mapToObj(x ->(char)x), '\0'));
        return Program.parse(new PeekTokenIterator(tokens.stream()));
    }

    public static ASTNode fromFile(String file) throws FileNotFoundException, UnsupportedEncodingException, LexicalException, ParserException {
        ArrayList<Token> tokens = Lexer.fromFile(file);
        return Program.parse(new PeekTokenIterator(tokens.stream()));
    }
}
