package com.company.Parser.Expression;

import com.company.Interpreter.Service.BinaryCalculation;
import com.company.Interpreter.Service.Variable;
import com.company.Interpreter.Value.ArrayValue.ArrayValue;
import com.company.Interpreter.Value.SingleValue.SingleValue;
import com.company.Interpreter.Value.VariableValue;
import com.company.Lexer.Service.Token;
import com.company.Lexer.Service.TokenType;
import com.company.Parser.Service.Expression;
import com.company.Interpreter.Service.FunctionCall;
import com.company.VI_Exception;


import java.util.ArrayList;


public class AddictiveExpression extends Expression
{

    private Token ID = null;


    public AddictiveExpression(Token token)
    {
        if(ID == null)
            ID = token;
    }

    @Override
    public void process() throws VI_Exception
    {
        Token currentToken;
//14


        int removePosition = -1, removeCount = 0;

        boolean functionStarted = false;
        int[] roundBrackets = {0, 0};

        AddictiveExpression currentExpression = null;

        for(int i = 0 ; i < expressionObjects.size() ; i ++)
        {
            currentToken = (Token)expressionObjects.get(i);

            if(currentToken.getTokenType() == TokenType.LEFT_ROUNDED_BRACKET)
            {
                roundBrackets[0]++;
                if(removePosition != -1)
                {
                    ++removeCount;
                    functionStarted = true;
                }

                if(currentExpression != null)
                    currentExpression.add(currentToken);
            }
            else if(currentToken.getTokenType() == TokenType.RIGHT_ROUNDED_BRACKET)
            {
                roundBrackets[1]++;
                if(roundBrackets[0] != roundBrackets[1])
                    if(functionStarted)
                        ++removeCount;

                if(currentExpression != null)
                {
                    if(functionStarted)
                        currentExpression.add(currentToken);



                    i-=removeCount;

                    for(int j = 0 ; j < removeCount ; j++)
                        expressionObjects.remove(removePosition);

                    removeCount = 0;


                    currentExpression.process();
                    functionStarted = false;
                    expressionObjects.set(removePosition, currentExpression);
                    currentExpression = null;

                    removePosition = -1;

                }
            }
            else if(currentToken.getTokenType() == TokenType.NUMBER)
            {
                if(removePosition != - 1)
                    ++removeCount;

                if(currentExpression != null)
                    currentExpression.add(currentToken);
            }
            else if(currentToken.getTokenType() == TokenType.ID)
            {
                if(currentExpression == null)
                {
                    currentExpression = new AddictiveExpression(currentToken);
                    removePosition = i;
                }
                else
                {
                    currentExpression.add(currentToken);
                    removeCount++;
                }
            }
            else
            {
                if(currentExpression != null)
                {
                    if(currentExpression.expressionObjects.size() == 0 && currentToken.getTokenType() != TokenType.COLON)
                    {
                        for(int j = 0 ; j < removeCount ; j++)
                            expressionObjects.remove(removePosition);

                        i-=removeCount;
                        removeCount = 0;


                        expressionObjects.set(removePosition, currentExpression);
                        currentExpression.process();
                        functionStarted = false;
                        expressionObjects.set(removePosition, currentExpression);
                        currentExpression = null;

                        removePosition = -1;
                    }
                    else
                    {
                        removeCount++;
                        currentExpression.add(currentToken);
                    }

                }
            }
        }

    }

    public Object processValue(Variable var) throws VI_Exception
    {
        VariableValue variableValue = var.getVariableValue();

        if(expressionObjects.size() == 0)
            if(var.getVariableType() != null)
                return variableValue.getValue();
            else
                throw new VI_Exception(var.getVariableName() + " : variable is not initialized or has nil value");
        else
            return processAddictiveExpression(var);
    }


    private Object processAddictiveExpression(Variable var) throws VI_Exception
    {
        Token currentToken;

        FunctionCall functionCall = null;


        int[] roundBrackets = {0, 0};

        Expression currentExpression = null;

        for (Object expressionObject : expressionObjects)
        {

            if(expressionObject instanceof Token)
            {
                currentToken = (Token) expressionObject;

                if (currentToken.getTokenType() == TokenType.LEFT_ROUNDED_BRACKET)
                {
                    roundBrackets[0]++;

                    if (functionCall != null)
                    {
                        if (functionCall.argumentsStarted)
                            functionCall.add(currentToken);
                        else
                            functionCall.argumentsStarted = true;
                    }
                }
                else if (currentToken.getTokenType() == TokenType.RIGHT_ROUNDED_BRACKET)
                {
                    roundBrackets[1]++;

                    if (roundBrackets[0] == roundBrackets[1])
                    {
                        if (functionCall != null && functionCall.argumentsStarted)
                            return functionCall.processFunctionCall(var);
                    }
                    else
                    {
                        assert functionCall != null;
                        functionCall.add(currentToken);
                    }

                }
                else if (currentToken.getTokenType() == TokenType.NUMBER)
                {
                    if (functionCall != null && functionCall.argumentsStarted)
                        functionCall.add(currentToken);

                }
                else
                {
                    if (currentToken.getTokenType() == TokenType.COLON)
                    {
                        functionCall = new FunctionCall();
                    }
                    else
                    {
                        if (functionCall != null)
                        {
                            if (functionCall.getFunctionCall() == null)
                                functionCall.setFunctionCall(currentToken);
                            else
                                functionCall.add(currentToken);
                        }
                        else if (currentExpression != null)
                            currentExpression.add(currentToken);
                    }
                }
            }
            else
            {
                if (functionCall != null && functionCall.argumentsStarted)
                    functionCall.add(expressionObject);
            }


        }

        return null;
    }


    public AddictiveExpression clone()
    {
        AddictiveExpression clone = new AddictiveExpression(this.ID);

        clone.expressionObjects = new ArrayList<>();

        for(Object o : expressionObjects)
            try
            {
                if(o instanceof AddictiveExpression)
                    clone.expressionObjects.add((((AddictiveExpression) o).clone()));
                else
                    clone.expressionObjects.add(o);
            }
            catch (Exception e) { e.printStackTrace(); }


        return clone;
    }

    public Token getID() { return ID; }
}