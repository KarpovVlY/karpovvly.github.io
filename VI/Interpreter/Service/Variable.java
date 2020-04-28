package com.company.Interpreter.Service;


import com.company.Interpreter.Value.VariableValue;
import com.company.Lexer.Service.TokenType;

public class Variable
{
    private String variableName;
    private TokenType variableType;
    private VariableValue variableValue;


    public Variable(String variableName, VariableValue variableValue)
    {
        this.variableName = variableName;
        this.variableValue = variableValue;
        this.variableType = variableValue.getValueType();
    }


    public String getVariableName() { return variableName; }

    public VariableValue getVariableValue() { return variableValue; }

    public TokenType getVariableType() { return variableType; }
}
