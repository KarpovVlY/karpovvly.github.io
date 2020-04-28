package com.company.Interpreter.Value.SingleValue;

import com.company.Interpreter.Value.VariableValue;
import com.company.Lexer.Service.Token;
import com.company.Lexer.Service.TokenType;


public class SingleValue extends VariableValue
{
    public Token getSingleValue() {
        return singleValue;
    }

    private final Token singleValue;

    public SingleValue(Token singleValue) { this.singleValue = singleValue; }


    @Override
    public TokenType getValueType() { return singleValue.getTokenType();}

    @Override
    public Token getValue()
    {
        return singleValue;
    }
}
