package com.company.Interpreter.Value.ArrayValue;

import com.company.Interpreter.Value.VariableValue;
import com.company.Lexer.Service.Token;
import com.company.Lexer.Service.TokenType;
import com.company.VI_Exception;


import java.util.ArrayList;

public class ArrayValue extends VariableValue
{
    private ArrayList<Object> arrayValue;


    public ArrayValue () {this.arrayValue = new ArrayList<>(); }

    @Override
    public TokenType getValueType() { return TokenType.ARRAY; }

    @Override
    public Object getValue() {return this; }

    public void add(Object value) { arrayValue.add(value); }

    public Object get(int position) { return arrayValue.get(position); }

    public void set( Object value, int position) { arrayValue.set(position, value); }

    public void remove(int position) { arrayValue.remove(position); }

    public void clear() { arrayValue.clear(); }

    public int getSize() { return arrayValue.size(); }

    public void setSize(int size)
    {
        arrayValue = new ArrayList<>();

        for(int i = 0 ; i < size ; i ++)
            arrayValue.add(new Token("0", TokenType.NUMBER));
    }

    public Token isEmpty()
    {
        if(arrayValue.isEmpty())
            return new Token("true", TokenType.BOOLEAN);
        else
            return new Token("false", TokenType.BOOLEAN);
    }
}

