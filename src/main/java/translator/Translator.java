package translator;

import lexer.Token;
import lexer.TokenType;
import org.apache.commons.lang3.NotImplementedException;
import parser.ast.ASTNode;
import parser.ast.ASTNodeTypes;
import parser.ast.FunctionDeclareStmt;
import parser.ast.IfStmt;
import parser.utils.ParserException;
import translator.symbol.Symbol;
import translator.symbol.SymbolTable;

public class Translator {

    public TAProgram translate(ASTNode astNode) throws ParserException {
        TAProgram program = new TAProgram();

        SymbolTable symbolTable = new SymbolTable();
        for (ASTNode child : astNode.getChildren()) {
            translateStmt(program, child, symbolTable);
        }

        program.setStaticSymbols(symbolTable);

        Token main = new Token(TokenType.VARIABLE, "main");

        if(symbolTable.exist(main)) {
            symbolTable.createVariable(); // 返回值
            program.add(new TAInstruction(TAInstructionType.SP, null, null,
                    -symbolTable.localSize(), null));
            program.add(new TAInstruction(
                    TAInstructionType.CALL, null, null,
                    symbolTable.cloneFromSymbolTree(main, 0),null ));
            program.add(new TAInstruction(TAInstructionType.SP, null, null,
                    symbolTable.localSize(), null));
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
            case IF_STMT:
                translateIfStmt(program, node, symbolTable);
                return;
            case BLOCK:
                translateBlock(program, node, symbolTable);
                return;
            case FUNCTION_DECLARE_STMT:
                translateFunctionDeclareStmt(program, node, symbolTable);
                return;
            case RETURN_STMT:
                translateReturnStmt(program, node, symbolTable);
                return;
            case CALL_EXPR:
                translateCallExpr(program, node, symbolTable);
                return;
        }
        throw new NotImplementedException("Translator not impl for" + node.getType());
    }

    private void translateReturnStmt(TAProgram program, ASTNode node, SymbolTable symbolTable) {
        Symbol returnValue = null;
        if (node.getChild(0) != null) {
            returnValue = translateExpr(program, node.getChild(0), symbolTable);
        }
        program.add(new TAInstruction(TAInstructionType.RETURN, null, null, returnValue, null));
    }

    private Symbol translateCallExpr(TAProgram program, ASTNode node, SymbolTable symbolTable) {
        // 函数名称 foo()
        ASTNode factor = node.getChild(0);
        // foo -> symbol(foo) L0
        // 返回值属于调用的活动记录
        Symbol returnValue = symbolTable.createVariable();
        // 作为返回地址
        symbolTable.createVariable();

        for (int i = 1; i < node.getChildren().size(); i++) {
            ASTNode expr = node.getChild(i);
            Symbol addr = translateExpr(program, expr, symbolTable);
            program.add(new TAInstruction(TAInstructionType.PARAM, null, null, addr, i - 1));
        }

        Symbol funcAddr = symbolTable.cloneFromSymbolTree(factor.getLexeme(), 0);

        // 栈指针的改变与调用完后的还原（函数内自己还有其他变量）
        program.add(new TAInstruction(TAInstructionType.SP, null, null, -symbolTable.localSize(), null));
        program.add(new TAInstruction(TAInstructionType.CALL, null, null, funcAddr, null));
        program.add(new TAInstruction(TAInstructionType.SP, null, null, symbolTable.localSize(), null));

        return returnValue;
    }

    private void translateFunctionDeclareStmt(TAProgram program, ASTNode node, SymbolTable parent) throws ParserException {
        TAInstruction label = program.addLabel();
        FunctionDeclareStmt func = (FunctionDeclareStmt) node;

        SymbolTable symbolTable = new SymbolTable();

        program.add(new TAInstruction(TAInstructionType.FUNC_BEGIN, null, null, null, null));
        symbolTable.createVariable(); // 返回地址

        label.setArg2(node.getLexeme());

        parent.createLabel((String) label.getArg1(), node.getLexeme());

        // 参数处理
        for (ASTNode arg : func.getArgs().getChildren()) {
            symbolTable.createSymbolByLexeme(arg.getLexeme());
        }
        // block处理
        for (ASTNode block : func.getBlock().getChildren()) {
            translateStmt(program, block, symbolTable);
        }
        parent.addChild(symbolTable);
    }

    private void translateIfStmt(TAProgram program, ASTNode node, SymbolTable symbolTable) throws ParserException {
        IfStmt ifStmt = (IfStmt) node;
        ASTNode expr = ifStmt.getExpr();
        Symbol exprAddr = translateExpr(program, expr, symbolTable);
        TAInstruction ifInstruction = new TAInstruction(TAInstructionType.IF, null, null, exprAddr, null);

        program.add(ifInstruction);

        translateBlock(program, ifStmt.getBlock(), symbolTable);

        // if(expr) {...} else if {...}
        TAInstruction gotoInstruction = null;
        if (node.getChild(2) != null) {
            gotoInstruction = new TAInstruction(TAInstructionType.GOTO, null, null, null, null);
            program.add(gotoInstruction);
            TAInstruction labelEndIf = program.addLabel();
            ifInstruction.setArg2(labelEndIf.getArg1());
        }
        if (ifStmt.getElseBlock() != null) {
            translateBlock(program, ifStmt.getElseBlock(), symbolTable);
        } else if (ifStmt.getElseIfStmt() != null) {
            translateIfStmt(program, ifStmt.getElseIfStmt(), symbolTable);
        }

        TAInstruction labelEnd = program.addLabel();

        if (node.getChild(2) == null) {
            ifInstruction.setArg2(labelEnd.getArg1());
        } else {
            gotoInstruction.setArg1(labelEnd.getArg1());
        }
    }

    private void translateBlock(TAProgram program, ASTNode node, SymbolTable parent) throws ParserException {
        SymbolTable symbolTable = new SymbolTable();
        parent.addChild(symbolTable);

        Symbol parentOffset = symbolTable.createVariable();
        parentOffset.setLexeme(new Token(TokenType.INTEGER, parent.localSize() + ""));

        // push record 压栈活动记录
        TAInstruction pushRecord = new TAInstruction(TAInstructionType.SP, null, null, null, null);
        program.add(pushRecord);

        for (ASTNode stmt : node.getChildren()) {
            translateStmt(program, stmt, symbolTable);
        }

        // 出栈活动记录
        TAInstruction popRecord = new TAInstruction(TAInstructionType.SP, null, null, null, null);
        program.add(popRecord);

        // 栈指针的减少过程(在压栈)
        pushRecord.setArg1(-parent.localSize());
        popRecord.setArg1(parent.localSize());
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
            Symbol temp = translateCallExpr(program, node, symbolTable);
            node.setProp("temp", temp);
            return temp;
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
