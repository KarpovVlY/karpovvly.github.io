package com.company.Parser.Expression;

import com.company.Lexer.Service.Token;
import com.company.Lexer.Service.TokenType;
import com.company.Parser.Service.Expression;
import com.company.VI_Exception;

public class TypeExpression extends Expression
{
    private TokenType typeExpression;

    public TypeExpression(Token currentToken) { typeExpression = currentToken.getTokenType(); }


    @Override
    public void add(Object o) throws VI_Exception
    {
        if (o instanceof Token)
            typeExpression = ((Token) o).getTokenType();
        else
            throw new VI_Exception("incorrect type");

    }

    public TokenType getTypeExpression() { return typeExpression; }
}
