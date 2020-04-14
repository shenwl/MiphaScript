package translator;

import lexer.Token;
import lexer.TokenType;
import translator.symbol.Symbol;
import translator.symbol.SymbolTable;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SymbolTest {
    @Test
    public void symbolTableTest() {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.createLabel("L0", new Token(TokenType.VARIABLE, "foo"));
        symbolTable.createVariable();
        symbolTable.createSymbolByLexeme(new Token(TokenType.VARIABLE, "a"));
        assertEquals(2, symbolTable.localSize());
    }

    @Test
    public void chainTest() {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.createSymbolByLexeme(new Token(TokenType.VARIABLE, "a"));

        SymbolTable childTable = new SymbolTable();
        symbolTable.addChild(childTable);

        SymbolTable childChildTable = new SymbolTable();
        childTable.addChild(childChildTable);

        assertTrue(childTable.exist(new Token(TokenType.VARIABLE, "a")));
        assertTrue(childChildTable.exist(new Token(TokenType.VARIABLE, "a")));
    }

    @Test
    public void symbolOffsetTest() {
        SymbolTable symbolTable = new SymbolTable();
        symbolTable.createSymbolByLexeme(new Token(TokenType.INTEGER, "100"));
        Symbol symbolA = symbolTable.createSymbolByLexeme(new Token(TokenType.VARIABLE, "a"));
        Symbol symbolB = symbolTable.createSymbolByLexeme(new Token(TokenType.VARIABLE, "b"));

        SymbolTable childTable = new SymbolTable();
        symbolTable.addChild(childTable);

        Symbol anotherSymbolB = childTable.createSymbolByLexeme(new Token(TokenType.VARIABLE, "b"));
        Symbol symbolC = childTable.createSymbolByLexeme(new Token(TokenType.VARIABLE, "c"));


        assertEquals(0, symbolA.getOffset());
        assertEquals(1, symbolB.getOffset());
        assertEquals(1, anotherSymbolB.getOffset());
        assertEquals(1, anotherSymbolB.getLayerOffset());
        assertEquals(0, symbolC.getOffset());
        assertEquals(0, symbolC.getLayerOffset());
    }
}
