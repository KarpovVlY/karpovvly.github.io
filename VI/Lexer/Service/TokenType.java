package com.company.Lexer.Service;

public enum TokenType
{
    NUMBER,
    BOOLEAN,

    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,
    PERCENT,

    LEFT_BRACKET,
    RIGHT_BRACKET,
    LEFT_ROUNDED_BRACKET,
    RIGHT_ROUNDED_BRACKET,
    LEFT_SQUARE_BRACKET,
    RIGHT_SQUARE_BRACKET,

    QUESTION_MARK,
    EXCLAMATION_MARK,
    EQUAL_MARK,

    LESS,
    MORE,
    LESS_EQUAL,
    MORE_EQUAL,
    POSITIVE_EQUAL,
    NEGATIVE_EQUAL,
    AND,
    OR,

    COMMA,
    TILDE,
    COLON,
    QUOTE,

    VARIABLE_DECLARATION,
    FUNCTION_DECLARATION,
    IF_STATEMENT,
    ELSE_STATEMENT,
    WHILE_STATEMENT,
    FOR_STATEMENT,
    ID,

    PRINT,
    PRINTLN,

    HASH,
    ARRAY,
    LIST,

    FUNCTION,
}
