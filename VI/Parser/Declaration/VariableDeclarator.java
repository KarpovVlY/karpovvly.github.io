package com.company.Parser.Declaration;

import com.company.Lexer.Service.Token;
import com.company.Lexer.Service.TokenType;
import com.company.Parser.Expression.AddictiveExpression;
import com.company.Parser.Expression.TypeExpression;
import com.company.Parser.Service.Expression;
import com.company.VI_Exception;

import java.util.ArrayList;

public class VariableDeclarator
{
    private Token ID = null;
    private Expression expression = new Expression();


    private VariableDeclarator(){}

    public VariableDeclarator(ArrayList<Token> tokens, VariableDeclaration parent) throws VI_Exception
    {
        Token currentToken = tokens.get(0);


        if(tokens.size() != 1)
        {
            if(currentToken.getTokenType() != TokenType.ID)
                throw new VI_Exception("missed id ");
            else ID = currentToken;

            Expression currentExpression = null;

            currentToken = tokens.get(1);

            if(currentToken.getTokenType() == TokenType.TILDE)
                currentExpression = new TypeExpression(currentToken);
            else if(currentToken.getTokenType() != TokenType.EQUAL_MARK)
                throw new VI_Exception("missed assignment sign");

            for(Object o : parent.call(2,currentExpression).getExpressionObjects())
                expression.add(o);

        }
        else if(currentToken.getTokenType() ==  TokenType.ID)
            ID = currentToken;
        else
            throw new VI_Exception("error in variable ID");


        expression.process();
    }



    public VariableDeclarator clone()
    {
        VariableDeclarator clone = new VariableDeclarator();

        clone.ID = this.ID;
        clone.expression = new Expression();

        for(Object o : this.expression.getExpressionObjects())
            try
            {
                if(o instanceof AddictiveExpression)
                    clone.expression.add((((AddictiveExpression) o).clone()));
                else
                    clone.expression.add(o);
            }
            catch (Exception e) { e.printStackTrace(); }

        return clone;
    }

    public Expression getExpression() { return expression; }
    public Token getID() { return ID; }

}
