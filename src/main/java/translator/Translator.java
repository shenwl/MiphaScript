package translator;

import lexer.Token;
import org.apache.commons.lang3.NotImplementedException;
import parser.ast.ASTNode;
import parser.ast.ASTNodeTypes;
import parser.utils.ParserException;
import translator.symbol.Symbol;
import translator.symbol.SymbolTable;

import java.text.ParseException;

public class Translator {

    public TAProgram translate(ASTNode astNode) throws ParserException {
        TAProgram program = new TAProgram();

        SymbolTable symbolTable = new SymbolTable();
        for (ASTNode child : astNode.getChildren()) {
            translateStmt(program, child, symbolTable);
        }

        return program;
    }

    public void translateStmt(TAProgram program, ASTNode node, SymbolTable symbolTable) throws ParserException {
        switch (node.getType()) {
            case ASSIGN_STMT:
                translateAssignStmt(program, node, symbolTable);
                return;
            case DECLARE_STMT:
                translateDeclareStmt(program, node, symbolTable);
                return;
        }
        throw new NotImplementedException("Translator not impl for" + node.getType());
    }

    private void translateAssignStmt(TAProgram program, ASTNode node, SymbolTable symbolTable) {
        // 2 * 3 + 1
        // p0 = 2 * 3 (temp)
        // p1 = p0 + 1 (p1为表达式结果)
        Symbol assigned = symbolTable.createSymbolByLexeme(node.getChild(0).getLexeme());
        ASTNode expr = node.getChild(1);
        Symbol temp = translateExpr(program, expr, symbolTable);
        program.add(new TAInstruction(
                TAInstructionType.ASSIGN,
                assigned,
                "=",
                temp,
                null
        ));
    }

    private void translateDeclareStmt(TAProgram program, ASTNode node, SymbolTable symbolTable) throws ParserException {
        // var a = 1
        Token lexeme = node.getChild(0).getLexeme();
        // 已被定义
        if (symbolTable.exist(lexeme)) {
            throw new ParserException("Syntax Error, Identifier" + lexeme.getValue() + "has already been declared");
        }
        Symbol assigned = symbolTable.createSymbolByLexeme(lexeme);
        ASTNode expr = node.getChild(1);
        Symbol temp = translateExpr(program, expr, symbolTable);
        program.add(new TAInstruction(
                TAInstructionType.ASSIGN,
                assigned,
                "=",
                temp,
                null
        ));
    }

    // SDD:
    //  E -> E1 op E2
    //  E -> F
    private Symbol translateExpr(TAProgram program, ASTNode node, SymbolTable symbolTable) {
        if (node.isValueType()) {
            Symbol temp = symbolTable.createSymbolByLexeme(node.getLexeme());
            node.setProp("temp", temp);
            return temp;
        }
        if (node.getType() == ASTNodeTypes.CALL_EXPR) {
            throw new NotImplementedException("CALL_EXPR not impl");
        }
        for (ASTNode child : node.getChildren()) {
            translateExpr(program, child, symbolTable);
        }
        if (node.getProp("temp") == null) {
            node.setProp("temp", symbolTable.createVariable());
        }

        TAInstruction instruction = new TAInstruction(
                TAInstructionType.ASSIGN,
                (Symbol) node.getProp("temp"),
                node.getLexeme().getValue(),
                node.getChild(0).getProp("temp"),
                node.getChild(1).getProp("temp")
        );
        program.add(instruction);
        return instruction.getResult();
    }
}
