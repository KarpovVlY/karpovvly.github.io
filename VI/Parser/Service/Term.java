package com.company.Parser.Service;


import com.company.Lexer.Service.Token;
import com.company.Lexer.Service.TokenType;
import com.company.Parser.Expression.AddictiveExpression;
import com.company.VI_Exception;


import java.util.ArrayList;

public class Term
{
    protected ArrayList<Token> tokens = new ArrayList<>();


    public ArrayList<Token> getTokens() { return tokens; }

    public void add(Token token) { tokens.add(token); }


    public Term clone() { return this; }

    public void process() throws VI_Exception { }


    protected Expression processTokens(int position, Expression current) throws VI_Exception
    {
        Expression expression = new Expression();
        Expression currentExpression = current;


        boolean functionCall = current != null;


        int[] roundBrackets = {0, 0};

        Token currentToken;


        for(; position<tokens.size() ; position++)
        {
            currentToken = tokens.get(position);

            if(currentToken.getTokenType() == TokenType.ID)
            {
                if(currentExpression == null)
                    currentExpression = new AddictiveExpression(currentToken);
                else
                    currentExpression.add(currentToken);
            }
            else
            {
                if(currentExpression == null)
                    expression.add(currentToken);
                else
                {
                    if(currentExpression instanceof AddictiveExpression)
                    {
                        if(currentToken.getTokenType() == TokenType.LEFT_ROUNDED_BRACKET)
                        {
                            roundBrackets[0]++;
                            currentExpression.add(currentToken);
                        }
                        else if(currentToken.getTokenType() == TokenType.RIGHT_ROUNDED_BRACKET)
                        {
                            roundBrackets[1]++;


                            if(roundBrackets[0] == roundBrackets[1])
                                functionCall = false;

                            if(currentExpression.getExpressionObjects().size() != 0)
                                currentExpression.add(currentToken);
                            else
                            {
                                currentExpression.process();
                                expression.add(currentExpression);
                                currentExpression = null;
                                expression.add(currentToken);
                            }

                        }
                        else if(currentToken.getTokenType() == TokenType.COLON)
                        {
                            currentExpression.add(currentToken);
                            functionCall = true;
                        }
                        else if(functionCall)
                        {
                            currentExpression.add(currentToken);
                        }
                        else
                        {
                            currentExpression.process();
                            expression.add(currentExpression);
                            currentExpression = null;
                            expression.add(currentToken);
                        }
                    }
                    else currentExpression.add(currentToken);
                }
            }
        }


        if(currentExpression != null)
        {
            currentExpression.process();
            expression.add(currentExpression);
        }

        return expression;
    }
}