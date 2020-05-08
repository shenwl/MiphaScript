package translator;

/**
 * 指令类型enum
 */
public enum TAInstructionType {
    ASSIGN,
    GOTO,
    IF,
    LABEL,
    CALL,
    RETURN,
    SP,         // 维护栈指针
    PARAM,      // 传参
    FUNC_BEGIN,
}
