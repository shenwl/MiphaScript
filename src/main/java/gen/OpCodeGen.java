package gen;

import gen.operand.ImmediateNumber;
import gen.operand.Label;
import gen.operand.Offset;
import gen.operand.Register;
import translator.TAInstruction;
import translator.TAProgram;
import translator.symbol.Symbol;

import java.util.ArrayList;
import java.util.Hashtable;

public class OpCodeGen {
    /**
     * 将三地址代码转化为OpCode
     * https://www.yuque.com/shenwl/nr0tg4/xuy3eb
     */
    public OpCodeProgram gen(TAProgram taProgram) {
        OpCodeProgram program = new OpCodeProgram();

        ArrayList<TAInstruction> taInstructions = taProgram.getInstructions();

        Hashtable<String, Integer> labelHash = new Hashtable<>();

        for (TAInstruction taInstruction : taInstructions) {
            program.addComment(taInstruction.toString());

            switch (taInstruction.getType()) {
                case ASSIGN:
                    genCopy(program, taInstruction);
                    break;
                case GOTO:
                    genGoto(program, taInstruction);
                    break;
                case CALL:
                    genCall(program, taInstruction);
                    break;
                case PARAM:
                    genPass(program, taInstruction);
                    break;
                case SP:
                    genSp(program, taInstruction);
                    break;
                case LABEL:
                    if (taInstruction.getArg2() != null && taInstruction.getArg2().equals("main")) {
                        program.setEntry(program.instructions.size());
                    }
                    labelHash.put((String) taInstruction.getArg1(), program.instructions.size());
                    break;
                case RETURN:
                    genReturn(program, taInstruction);
                    break;
                case FUNC_BEGIN:
                    genFuncBegin(program, taInstruction);
                    break;
                case IF:
                    genIf(program, taInstruction);
                    break;
            }
        }

        // 翻译完relabel一下
        relabel(program, labelHash);

        return program;
    }

    /**
     * 重新计算label偏移量
     */
    private void relabel(OpCodeProgram program, Hashtable<String, Integer> labelHash) {
        program.instructions.forEach(instruction -> {
            OpCode opCode = instruction.getOpCode();
            if (opCode == OpCode.JUMP || opCode == OpCode.JR || opCode == OpCode.BNE) {
                int i = opCode == OpCode.BNE ? 2 : 0;
                Label labelOperand = (Label) instruction.opList.get(i);
                String label = labelOperand.getLabel();
                Integer offset = labelHash.get(label);
                labelOperand.setOffset(offset);
            }
        });
    }

    private void genIf(OpCodeProgram program, TAInstruction taInstruction) {
        String label = (String) taInstruction.getArg2();
        program.add(Instruction.bne(Register.S2, Register.ZERO, label));
    }

    private void genFuncBegin(OpCodeProgram program, TAInstruction taInstruction) {
        Instruction i = Instruction.offsetInstruction(OpCode.SW, Register.RA, Register.SP, new Offset(0));
        program.add(i);
    }

    private void genReturn(OpCodeProgram program, TAInstruction taInstruction) {
        Symbol ret = (Symbol) taInstruction.getArg1();
        if (ret != null) {
            program.add(Instruction.loadToRegister(Register.S0, ret));
        }
        program.add(Instruction.offsetInstruction(
                OpCode.SW, Register.S0, Register.SP, new Offset(1)
        ));
        Instruction i = new Instruction(OpCode.RETURN);
        program.add(i);
    }

    private void genSp(OpCodeProgram program, TAInstruction taInstruction) {
        int offset = (int) taInstruction.getArg1();
        if (offset > 0) {
            program.add(Instruction.immediate(OpCode.ADDI, Register.SP, new ImmediateNumber(offset)));
        } else {
            program.add(Instruction.immediate(OpCode.SUBI, Register.SP, new ImmediateNumber(-offset)));
        }
    }

    private void genPass(OpCodeProgram program, TAInstruction taInstruction) {
        // PASS 10
        // PASS 100
        // createVariable() return value
        // CALL fn
        Symbol arg1 = (Symbol) taInstruction.getArg1();
        int num = (int) taInstruction.getArg2();

        program.add(Instruction.loadToRegister(Register.S0, arg1));
        // PASS a
        program.add(Instruction.offsetInstruction(
                OpCode.SW, Register.S0, Register.SP, new Offset(-(num))
        ));
    }

    private void genCall(OpCodeProgram program, TAInstruction taInstruction) {
        Symbol label = (Symbol) taInstruction.getArg1();
        Instruction i = new Instruction(OpCode.JR); // 与JUMP的区别是RA中会存PC的值 RA <- PC
        i.opList.add(new Label(label.getLabel()));
        program.add(i);
    }

    private void genGoto(OpCodeProgram program, TAInstruction taInstruction) {
        String label = (String) taInstruction.getArg1();
        Instruction i = new Instruction(OpCode.JUMP);
        // tips: label对应的位置(offset)在relabel阶段计算
        i.opList.add(new Label(label));
        program.add(i);
    }

    private void genCopy(OpCodeProgram program, TAInstruction taInstruction) {
        Symbol result = taInstruction.getResult();
        String op = taInstruction.getOp();
        Symbol arg1 = (Symbol) taInstruction.getArg1();
        Symbol arg2 = (Symbol) taInstruction.getArg2();

        if (arg2 == null) {
            // result = arg1
            program.add(Instruction.loadToRegister(Register.S0, arg1));
            program.add(Instruction.saveToMemory(Register.S0, result));
        } else {
            // result = arg1 op arg2
            program.add(Instruction.loadToRegister(Register.S0, arg1));
            program.add(Instruction.loadToRegister(Register.S1, arg2));
            switch (op) {
                case "+":
                    program.add(Instruction.register(OpCode.ADD, Register.S2, Register.S0, Register.S1));
                    break;
                case "-":
                    program.add(Instruction.register(OpCode.SUB, Register.S2, Register.S0, Register.S1));
                    break;
                case "*":
                    program.add(Instruction.register(OpCode.MULT, Register.S0, Register.S1, null));
                    program.add(Instruction.register(OpCode.MFLO, Register.S2, null, null));
                    break;
                case "==":
                    program.add(Instruction.register(OpCode.EQ, Register.S2, Register.S0, Register.S1));
                    break;
            }
            program.add(Instruction.saveToMemory(Register.S2, result));
        }
    }
}
