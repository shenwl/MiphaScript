package parser.ast;

public enum ASTNodeTypes {
    BLOCK,                  // 代码块
    BINARY_EXPR,            // 二元表达式 (a+b)
    UNARY_EXPR,             // 一元表达式 (++i)
    VARIABLE,               // 变量
    SCALAR,                 // 标量(值类型)
    IF_STMT,                // if语句
    WHILE_STMT,
    FOR_STMT,
    ASSIGN_STMT,            // 赋值语句
    DECLARE_STMT,
    FUNCTION_DECLARE_STMT,  // 函数定义语句
    RETURN_STMT,            // 返回语句
    CALL_EXPR,              // 函数调用语句
}
