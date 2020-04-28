package com.company.Interpreter.Service;



import com.company.Lexer.Service.Token;
import com.company.Lexer.Service.TokenType;
import com.company.VI_Exception;

import java.util.ArrayList;
import java.util.Stack;

public class BinaryCalculation
{
    private Token result;

    public BinaryCalculation(ArrayList<Token> binaryTokens) throws VI_Exception
    {
        new SimplificationBinary().simplify(binaryTokens);

        result = calculateRPN(createRPN(binaryTokens));
    }

    public Token getResult() { return result; }


    private Token calculateRPN(ArrayList<Token> tokensRPN) throws VI_Exception
    {
        Stack<Token> RPN = new Stack<>();

        try
        {
            for (Token binaryToken : tokensRPN)
            {
                if (binaryToken.getTokenType() == TokenType.NUMBER || binaryToken.getTokenType() == TokenType.BOOLEAN)
                    RPN.push(binaryToken);
                else
                {
                    if (binaryToken.getTokenType() == TokenType.PLUS)
                    {
                        RPN.push(new Token(Double.toString(Double.parseDouble(RPN.pop().getContent())
                                + Double.parseDouble(RPN.pop().getContent())), TokenType.NUMBER));
                    }
                    else if (binaryToken.getTokenType() == TokenType.MINUS)
                    {
                        double buffer = Double.parseDouble(RPN.pop().getContent());
                        RPN.push(new Token(Double.toString(Double.parseDouble(RPN.pop().getContent())
                                - buffer), TokenType.NUMBER));
                    }
                    else if (binaryToken.getTokenType() == TokenType.MULTIPLY)
                    {
                        RPN.push(new Token(Double.toString(Double.parseDouble(RPN.pop().getContent())
                                * Double.parseDouble(RPN.pop().getContent())), TokenType.NUMBER));
                    }
                    else if (binaryToken.getTokenType() == TokenType.DIVIDE)
                    {

                        double buffer = Double.parseDouble(RPN.pop().getContent());
                        if (buffer != 0.0)
                            RPN.push(new Token(Double.toString(Double.parseDouble(RPN.pop().getContent())
                                    / buffer), TokenType.NUMBER));
                        else
                            throw new VI_Exception("0 : divine error");

                    }
                    else if (binaryToken.getTokenType() == TokenType.PERCENT)
                    {
                        double buffer = Double.parseDouble(RPN.pop().getContent());
                        RPN.push(new Token(Double.toString(Double.parseDouble(RPN.pop().getContent())
                                % buffer), TokenType.NUMBER));
                    }
                    else if (binaryToken.getTokenType() == TokenType.POSITIVE_EQUAL)
                    {
                        Token left = RPN.pop();
                        Token right = RPN.pop();

                        if (left.getTokenType() == right.getTokenType())
                        {
                            try
                            {
                                RPN.push(new Token(Boolean.toString(Double.parseDouble(left.getContent())
                                        == Double.parseDouble(right.getContent())), TokenType.BOOLEAN));
                            }
                            catch (Exception ignored)
                            {
                                RPN.push(new Token(Boolean.toString(Boolean.parseBoolean(left.getContent())
                                        == Boolean.parseBoolean(right.getContent())), TokenType.BOOLEAN));
                            }
                        }
                        else
                            throw new VI_Exception(left.getContent() + " == " + right.getContent() + " : type mismatch");

                    }
                    else if (binaryToken.getTokenType() == TokenType.NEGATIVE_EQUAL)
                    {
                        Token left = RPN.pop();
                        Token right = RPN.pop();

                        if (left.getTokenType() == right.getTokenType())
                        {
                            try
                            {
                                RPN.push(new Token(Boolean.toString(Double.parseDouble(left.getContent())
                                        != Double.parseDouble(right.getContent())), TokenType.BOOLEAN));
                            }
                            catch (Exception ignored)
                            {
                                RPN.push(new Token(Boolean.toString(Boolean.parseBoolean(left.getContent())
                                        != Boolean.parseBoolean(right.getContent())), TokenType.BOOLEAN));
                            }
                        }
                        else
                            throw new VI_Exception(left.getContent() + " == " + right.getContent() + " : type mismatch");
                    }
                    else if (binaryToken.getTokenType() == TokenType.MORE)
                    {
                        RPN.push(new Token(Boolean.toString(Double.parseDouble(RPN.pop().getContent())
                                < Double.parseDouble(RPN.pop().getContent())), TokenType.BOOLEAN));
                    }
                    else if (binaryToken.getTokenType() == TokenType.MORE_EQUAL)
                    {
                        RPN.push(new Token(Boolean.toString(Double.parseDouble(RPN.pop().getContent())
                                <= Double.parseDouble(RPN.pop().getContent())), TokenType.BOOLEAN));
                    }
                    else if (binaryToken.getTokenType() == TokenType.LESS)
                    {
                        RPN.push(new Token(Boolean.toString(Double.parseDouble(RPN.pop().getContent())
                                > Double.parseDouble(RPN.pop().getContent())), TokenType.BOOLEAN));
                    }
                    else if (binaryToken.getTokenType() == TokenType.LESS_EQUAL)
                    {
                        RPN.push(new Token(Boolean.toString(Double.parseDouble(RPN.pop().getContent())
                                >= Double.parseDouble(RPN.pop().getContent())), TokenType.BOOLEAN));
                    }
                    else if (binaryToken.getTokenType() == TokenType.EXCLAMATION_MARK)
                    {
                        Token buffer = RPN.pop();

                        if (buffer.getTokenType() == TokenType.BOOLEAN)
                        {
                            if (buffer.getContent().compareTo("true") == 0)
                                RPN.push(new Token("false", TokenType.BOOLEAN));
                            else
                                RPN.push(new Token("true", TokenType.BOOLEAN));
                        }
                        else
                            throw new VI_Exception("unavailable cast ! to NUMBER");
                    }
                    else if (binaryToken.getTokenType() == TokenType.AND)
                    {
                        Token left = RPN.pop();
                        Token right = RPN.pop();

                        if (left.getTokenType() == TokenType.BOOLEAN && right.getTokenType() == TokenType.BOOLEAN)
                        {
                            RPN.push(new Token(Boolean.toString(Boolean.parseBoolean(left.getContent())
                                    && Boolean.parseBoolean(right.getContent())), TokenType.BOOLEAN));
                        }
                        else
                            throw new VI_Exception(left.getContent() + " == " + right.getContent() + " : type mismatch");
                    }
                    else if (binaryToken.getTokenType() == TokenType.OR)
                    {
                        Token left = RPN.pop();
                        Token right = RPN.pop();

                        if (left.getTokenType() == TokenType.BOOLEAN && right.getTokenType() == TokenType.BOOLEAN)
                        {
                            RPN.push(new Token(Boolean.toString(Boolean.parseBoolean(left.getContent())
                                    || Boolean.parseBoolean(right.getContent())), TokenType.BOOLEAN));
                        }
                        else
                            throw new VI_Exception(left.getContent() + " == " + right.getContent() + " : type mismatch");
                    }
                    else
                        throw new VI_Exception("unknown operand");
                }


            }
        }
        catch (Exception e) { throw new VI_Exception("unavailable operand cast"); }

        return RPN.get(0);
    }


    private ArrayList<Token> createRPN(ArrayList<Token> expressionTokens) throws VI_Exception
    {
        ArrayList<Token> RPN = new ArrayList<>();

        Stack<Token> operandsStack = new Stack<>();

        Token token;

        for (Token currentToken : expressionTokens)
        {
            if (currentToken.getTokenType() == TokenType.NUMBER || currentToken.getTokenType() == TokenType.BOOLEAN)
            {
                RPN.add(currentToken);
            }
            else
            {
                if (checkNumberRPN(currentToken.getTokenType()))
                {
                    if (operandsStack.size() > 0)
                    {
                        token = operandsStack.get(operandsStack.size() - 1);

                        while (operandsStack.size() > 0 && token.getTokenType() != TokenType.LEFT_ROUNDED_BRACKET)
                        {
                            if (operandPriority(currentToken) <= operandPriority(token))
                                RPN.add(operandsStack.pop());
                            else
                                break;
                        }

                    }

                    operandsStack.push(currentToken);
                }
                else if (currentToken.getTokenType() == TokenType.LEFT_ROUNDED_BRACKET)
                {
                    operandsStack.push(currentToken);
                }
                else if (currentToken.getTokenType() == TokenType.RIGHT_ROUNDED_BRACKET)
                {
                    token = operandsStack.get(operandsStack.size() - 1);

                    while (token.getTokenType() != TokenType.LEFT_ROUNDED_BRACKET)
                    {
                        RPN.add(operandsStack.pop());
                        token = operandsStack.get(operandsStack.size() - 1);

                        if (operandsStack.size() == 0)
                            throw new VI_Exception("No starting quote");

                        if (token.getTokenType() == TokenType.LEFT_ROUNDED_BRACKET)
                            operandsStack.pop();
                    }

                }
                else
                    throw new VI_Exception("error in processing binary calculations");
            }
        }

        while(operandsStack.size() > 0)
        {
            token = operandsStack.get(operandsStack.size()-1);
            RPN.add(operandsStack.pop());

            if (token.getTokenType() == TokenType.LEFT_ROUNDED_BRACKET)
                throw new VI_Exception("No ending quote");

        }

        return RPN;
    }


    private int operandPriority(Token operand)
    {
        if(operand.getTokenType() == TokenType.MULTIPLY) return 5;
        else if(operand.getTokenType() == TokenType.DIVIDE) return 5;
        else if(operand.getTokenType() == TokenType.PERCENT) return 5;
        else if(operand.getTokenType() == TokenType.PLUS) return 2;
        else if(operand.getTokenType() == TokenType.MINUS) return 2;

        else if(operand.getTokenType() == TokenType.EXCLAMATION_MARK) return 4;

        else if(operand.getTokenType() == TokenType.POSITIVE_EQUAL) return 1;
        else if(operand.getTokenType() == TokenType.NEGATIVE_EQUAL) return 1;
        else if(operand.getTokenType() == TokenType.MORE_EQUAL) return 1;
        else if(operand.getTokenType() == TokenType.LESS_EQUAL) return 1;
        else if(operand.getTokenType() == TokenType.MORE) return 1;
        else if(operand.getTokenType() == TokenType.LESS) return 1;

        else if(operand.getTokenType() == TokenType.AND) return 0;
        else if(operand.getTokenType() == TokenType.OR) return 0;

        return 0;
    }



    private boolean checkNumberRPN(TokenType tokenType)
    {
        return tokenType == TokenType.MULTIPLY
                || tokenType == TokenType.DIVIDE
                || tokenType == TokenType.PERCENT
                || tokenType == TokenType.PLUS
                || tokenType == TokenType.MINUS

                || tokenType == TokenType.POSITIVE_EQUAL
                || tokenType == TokenType.NEGATIVE_EQUAL
                || tokenType == TokenType.MORE_EQUAL
                || tokenType == TokenType.MORE
                || tokenType == TokenType.LESS_EQUAL
                || tokenType == TokenType.LESS

                || tokenType == TokenType.EXCLAMATION_MARK
                || tokenType == TokenType.AND
                || tokenType == TokenType.OR;
    }
}

class SimplificationBinary
{
    public SimplificationBinary(){}

    public void simplify(ArrayList<Token> tokens)
    {
        Token currentToken;
        Token previousToken = null;

        boolean hasMDP = false;

        for(int i = 0 ; i < tokens.size() ; i ++)
        {
            currentToken = tokens.get(i);

            if (previousToken != null)
            {
                if (previousToken.getTokenType() == TokenType.MINUS)
                {
                    if (currentToken.getTokenType() == TokenType.PLUS)
                    {
                        Token minus = new Token("-", TokenType.MINUS);
                        tokens.remove(i);
                        currentToken = minus;
                        --i;
                    }
                    else if (currentToken.getTokenType() == TokenType.MINUS)
                    {
                        Token plus = new Token("+", TokenType.PLUS);
                        tokens.remove(i - 1);
                        tokens.set(i - 1, plus);
                        currentToken = plus;
                        --i;
                    }
                    else if(currentToken.getTokenType() == TokenType.ID || currentToken.getTokenType() == TokenType.NUMBER)
                    {
                        if(hasMDP)
                        {
                            tokens.set(i, new Token(previousToken.getContent() + currentToken.getContent(), currentToken.getTokenType()));
                            tokens.remove(i - 1);
                            --i;
                            hasMDP = false;
                        }
                    }

                }
                else if (previousToken.getTokenType() == TokenType.PLUS)
                {
                    if (currentToken.getTokenType() == TokenType.PLUS || currentToken.getTokenType() == TokenType.MINUS)
                    {
                        tokens.remove(i - 1);
                        --i;
                    }
                    else if(currentToken.getTokenType() == TokenType.ID || currentToken.getTokenType() == TokenType.NUMBER)
                    {
                        if(hasMDP)
                        {
                            tokens.set(i, new Token(previousToken.getContent() + currentToken.getContent(), TokenType.NUMBER));
                            tokens.remove(i - 1);
                            --i;
                            hasMDP = false;
                        }
                    }
                }
                else if (currentToken.getTokenType() == TokenType.MULTIPLY
                        || currentToken.getTokenType() == TokenType.DIVIDE
                        || currentToken.getTokenType() == TokenType.PERCENT
                        || currentToken.getTokenType() == TokenType.LEFT_ROUNDED_BRACKET)
                {
                    hasMDP = true;
                }
                else if (currentToken.getTokenType() == TokenType.NUMBER
                        || currentToken.getTokenType() == TokenType.ID)
                {
                    hasMDP = false;
                }
            }
            else if (currentToken.getTokenType() == TokenType.MINUS
                    || currentToken.getTokenType() == TokenType.PLUS
                    || currentToken.getTokenType() == TokenType.LEFT_ROUNDED_BRACKET)
            {
                hasMDP = true;
            }



            previousToken = currentToken;
        }
    }
}