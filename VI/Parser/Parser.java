package com.company.Parser;

import com.company.Lexer.Service.Token;
import com.company.Lexer.Service.TokenType;
import com.company.Parser.Declaration.VariableDeclaration;
import com.company.Parser.Service.Term;
import com.company.Parser.Statement.*;
import com.company.VI_Exception;

import java.util.ArrayList;

public class Parser
{
    private final ArrayList<Token> tokens;

    private Term term = null;

    private int position = 0;
    public boolean blockFinished = true;
    private int[] brackets;

    private final CorrectSequence correctSequence;



    public Parser(ArrayList<Token> tokens) throws VI_Exception
    {
        this.tokens = tokens;
        this.correctSequence = new CorrectSequence();

        processInput();
    }


    
    private void processInput() throws VI_Exception
    {
        Token currentToken;

        for(; position < tokens.size() ; position ++)
        {
            currentToken = tokens.get(position);

            if(term == null)
            {
                position += 1;
                if(currentToken.getTokenType() == TokenType.VARIABLE_DECLARATION)
                {
                    term = new VariableDeclaration();
                    processVariableDeclaration();
                }
                else if(currentToken.getTokenType() == TokenType.ID)
                {
                    term = new AssignmentStatement(currentToken);
                    processAssignmentExpression();
                }
                else if(currentToken.getTokenType() == TokenType.IF_STATEMENT)
                {
                    term = new IfStatement();
                    processIfStatement();
                }
                else if(currentToken.getTokenType() == TokenType.WHILE_STATEMENT)
                {
                    term = new WhileStatement();
                    processWhileStatement();
                }
                else if(currentToken.getTokenType() == TokenType.FOR_STATEMENT)
                {
                    term = new ForStatement();
                    processForStatement();
                }
                else if(currentToken.getTokenType() == TokenType.PRINT)
                {
                    term = new PrintStatement();
                    processPrintStatement();
                }
                else if(currentToken.getTokenType() == TokenType.PRINTLN)
                {
                    term = new PrintlnStatement();
                    processPrintStatement();
                }
                else
                    throw new VI_Exception("Unknown command");

            }
        }

        term.process();
    }


    private void processVariableDeclaration() throws VI_Exception
    {
        Token currentToken;

        for( ; position < tokens.size() - 1 ; position ++)
        {
            currentToken = tokens.get(position);

            if(!correctSequence.checkSequence(currentToken.getTokenType(), tokens.get(position + 1).getTokenType()))
                throw new VI_Exception(currentToken, tokens.get(position + 1));
            else
                term.add(currentToken);

        }

        if(correctSequence.checkLastSequencePart(tokens.get(position).getTokenType()))
            term.add(tokens.get(position));
        else
            throw new VI_Exception(tokens.get(position));

    }



    private void processAssignmentExpression() throws VI_Exception
    {
        Token currentToken;

        for( ; position < tokens.size() - 1 ; position ++)
        {
            currentToken = tokens.get(position);

            if(!correctSequence.checkSequence(currentToken.getTokenType(), tokens.get(position + 1).getTokenType()))
                throw new VI_Exception(currentToken, tokens.get(position + 1));
            else
                term.add(currentToken);
        }

        try
        {
            if(correctSequence.checkLastSequencePart(tokens.get(position).getTokenType()))
                term.add(tokens.get(position));
            else
                throw new VI_Exception(tokens.get(position));
        }
        catch (Exception e) { throw new VI_Exception("incorrect statement"); }

    }




    private void processPrintStatement() throws VI_Exception
    {
        Token currentToken;

        boolean hasLeftBracket = false;

        for( ; position < tokens.size() - 1; position ++)
        {
            currentToken = tokens.get(position);

            if(!hasLeftBracket)
            {
                if(currentToken.getTokenType() == TokenType.LEFT_ROUNDED_BRACKET)
                    hasLeftBracket = true;
                else
                    throw  new VI_Exception("( : missed in processing print");
            }
            else
            {
                if(!correctSequence.checkSequence(currentToken.getTokenType(), tokens.get(position + 1).getTokenType()))
                    throw new VI_Exception(currentToken, tokens.get(position + 1));
                else
                    term.add(currentToken);
            }
        }

        currentToken = tokens.get(position);

        if(correctSequence.checkLastSequencePart(currentToken.getTokenType()))
            if(currentToken.getTokenType() != TokenType.RIGHT_ROUNDED_BRACKET)
                throw new VI_Exception(") : missed in processing print ");
    }




    private void processForStatement() throws VI_Exception
    {
        brackets = new int[]{0, 0};
        int[] roundedBrackets = {1 , 0};

        boolean paramsStarted = false;
        Token currentToken;

        for( ; position < tokens.size() - 1 ; position++)
        {
            currentToken = tokens.get(position);

            if (!paramsStarted)
            {
                if (currentToken.getTokenType() == TokenType.LEFT_ROUNDED_BRACKET)
                    paramsStarted = true;
                else
                    throw  new VI_Exception("( : missed in processing for");
            }
            else
            {
                if (currentToken.getTokenType() == TokenType.LEFT_ROUNDED_BRACKET)
                    ++roundedBrackets[0];
                else if (currentToken.getTokenType() == TokenType.RIGHT_ROUNDED_BRACKET)
                    ++roundedBrackets[1];

                term.add(currentToken);
            }
        }

        currentToken = tokens.get(position);

        if(correctSequence.checkLastSequencePart(currentToken.getTokenType()))
            if(currentToken.getTokenType() == TokenType.RIGHT_ROUNDED_BRACKET)
                ++roundedBrackets[1];
            else
                throw new VI_Exception(") : missed in processing for ");


        if(roundedBrackets[0] == roundedBrackets[1])
            blockFinished = false;
        else
            throw new VI_Exception("for params : mismatch in counting brackets");
    }





    private void processIfStatement() throws VI_Exception
    {
        brackets = new int[]{0, 0};
        int[] roundedBrackets = {1, 0};
        boolean paramsStarted = false;


        Token currentToken;

        for( ; position < tokens.size() - 1 ; position++)
        {
            currentToken = tokens.get(position);

            if(correctSequence.checkSequence(currentToken.getTokenType(), tokens.get(position + 1).getTokenType()))
            {
                if(!paramsStarted)
                {
                    if(currentToken.getTokenType() == TokenType.LEFT_ROUNDED_BRACKET)
                        paramsStarted = true;
                    else
                        throw  new VI_Exception("( : missed in processing if");
                }
                else
                {
                    if(currentToken.getTokenType() == TokenType.LEFT_ROUNDED_BRACKET)
                    {
                        ++roundedBrackets[0];
                        term.add(currentToken);
                    }
                    else if(currentToken.getTokenType() == TokenType.RIGHT_ROUNDED_BRACKET)
                    {
                        ++roundedBrackets[1];
                        term.add(currentToken);
                    }
                    else
                        term.add(currentToken);
                }
            }
            else
                throw new VI_Exception(currentToken, tokens.get(position + 1));

        }

        currentToken = tokens.get(position);

        if(correctSequence.checkLastSequencePart(currentToken.getTokenType()))
            if(currentToken.getTokenType() == TokenType.RIGHT_ROUNDED_BRACKET)
                ++roundedBrackets[1];
            else
                throw new VI_Exception(") : missed in processing if ");


        if(roundedBrackets[0] == roundedBrackets[1])
            blockFinished = false;
        else
            throw new VI_Exception("if params : mismatch in counting brackets");

    }


    private void processWhileStatement() throws VI_Exception
    {
        brackets = new int[]{0, 0};
        int[] roundedBrackets = {1, 0};
        boolean paramsStarted = false;


        Token currentToken;

        for( ; position < tokens.size() - 1 ; position++)
        {
            currentToken = tokens.get(position);

            if(correctSequence.checkSequence(currentToken.getTokenType(), tokens.get(position + 1).getTokenType()))
            {
                if(!paramsStarted)
                {
                    if(currentToken.getTokenType() == TokenType.LEFT_ROUNDED_BRACKET)
                        paramsStarted = true;
                    else
                        throw  new VI_Exception("( : missed in processing while");
                }
                else
                {
                    if(currentToken.getTokenType() == TokenType.LEFT_ROUNDED_BRACKET)
                    {
                        ++roundedBrackets[0];
                        term.add(currentToken);
                    }
                    else if(currentToken.getTokenType() == TokenType.RIGHT_ROUNDED_BRACKET)
                    {
                        ++roundedBrackets[1];
                        term.add(currentToken);
                    }
                    else
                        term.add(currentToken);
                }
            }
            else
                throw new VI_Exception(currentToken, tokens.get(position + 1));

        }

        currentToken = tokens.get(position);

        if(correctSequence.checkLastSequencePart(currentToken.getTokenType()))
            if(currentToken.getTokenType() == TokenType.RIGHT_ROUNDED_BRACKET)
                ++roundedBrackets[1];
            else
                throw new VI_Exception(") : missed in processing while ");


        if(roundedBrackets[0] == roundedBrackets[1])
            blockFinished = false;
        else
            throw new VI_Exception("while params : mismatch in counting brackets");

    }



    public int fillBlock(ArrayList<Token> tokens, int position) throws VI_Exception
    {
        if(term instanceof WhileStatement)
        {

            if(((WhileStatement) term).getBlock() == null)
            {
                if(tokens.size() == 1 && tokens.get(0).getTokenType() == TokenType.LEFT_BRACKET)
                {
                    ((WhileStatement) term).initBlock();
                    ++brackets[0];
                }
                else
                    throw new VI_Exception("Error in processing while block");
            }
            else
            {
                if(tokens.size() == 1)
                {
                    if(tokens.get(0).getTokenType() == TokenType.RIGHT_BRACKET)
                    {
                        ++brackets[1];

                        if(brackets[0] == brackets[1])
                        {
                            blockFinished = true;
                            ((WhileStatement) term).processBlock();
                        }
                        else
                        {
                            ((WhileStatement) term).addToBlock(tokens);
                        }

                    }
                    else if(tokens.get(0).getTokenType() == TokenType.LEFT_BRACKET)
                    {
                        ++brackets[0];
                        ((WhileStatement) term).addToBlock(tokens);
                    }
                    else if(tokens.get(0).getTokenType() == TokenType.ELSE_STATEMENT)
                    {
                        ((WhileStatement) term).addToBlock(tokens);
                    }
                    else
                        throw new VI_Exception("Error in counting brackets");
                }
                else
                {
                    ((WhileStatement) term).addToBlock(tokens);
                }
            }

        }
        else  if(term instanceof IfStatement)
        {

            if(((IfStatement) term).getBlock() == null)
            {
                if(tokens.size() == 1)
                {
                    if(tokens.get(0).getTokenType() == TokenType.LEFT_BRACKET)
                    {
                        ((IfStatement) term).initBlock();
                        ++brackets[0];
                    }
                    else if(!((IfStatement) term).isFullPositive())
                    {
                        if (tokens.get(0).getTokenType() != TokenType.ELSE_STATEMENT)
                            throw new VI_Exception("F missed in processing if block");
                    }
                    else
                    {
                        if (tokens.get(0).getTokenType() != TokenType.ELSE_STATEMENT)
                            throw new VI_Exception("error in processing if block");
                    }
                }
                else if(((IfStatement) term).isFullPositive())
                {
                    blockFinished = true;
                    return position - 1;
                }
                else
                    throw new VI_Exception("error in processing if block");

            }
            else
            {
                if(tokens.size() == 1)
                {
                    if(tokens.get(0).getTokenType() == TokenType.RIGHT_BRACKET)
                    {
                        ++brackets[1];

                        if(brackets[0] == brackets[1])
                        {
                            ((IfStatement) term).processBlock();

                            if(!((IfStatement)term).isFullNegative())
                                blockFinished = true;
                        }
                        else
                        {
                            ((IfStatement) term).addToBlock(tokens);
                        }

                    }
                    else if(tokens.get(0).getTokenType() == TokenType.LEFT_BRACKET)
                    {
                        ++brackets[0];
                        ((IfStatement) term).addToBlock(tokens);
                    }
                    else if(tokens.get(0).getTokenType() == TokenType.ELSE_STATEMENT)
                    {
                        ((IfStatement) term).addToBlock(tokens);
                    }
                    else
                        throw new VI_Exception("if block : mismatch in counting brackets");
                }
                else
                {
                    ((IfStatement) term).addToBlock(tokens);
                }
            }

        }
        else if(term instanceof ForStatement)
        {

            if(((ForStatement) term).getForWhileStatement().getBlock() == null)
            {
                if(tokens.size() == 1 && tokens.get(0).getTokenType() == TokenType.LEFT_BRACKET)
                {
                    ((ForStatement) term).getForWhileStatement().initBlock();
                    ++brackets[0];
                }
                else
                    throw new VI_Exception("Error in processing for block");
            }
            else
            {
                if(tokens.size() == 1)
                {
                    if(tokens.get(0).getTokenType() == TokenType.RIGHT_BRACKET)
                    {
                        ++brackets[1];

                        if(brackets[0] == brackets[1])
                        {
                            blockFinished = true;
                            ((ForStatement) term).getForWhileStatement().processBlock();
                        }
                        else
                        {
                            ((ForStatement) term).getForWhileStatement().addToBlock(tokens);
                        }

                    }
                    else if(tokens.get(0).getTokenType() == TokenType.LEFT_BRACKET)
                    {
                        ++brackets[0];
                        ((ForStatement) term).getForWhileStatement().addToBlock(tokens);
                    }
                    else if(tokens.get(0).getTokenType() == TokenType.ELSE_STATEMENT)
                    {
                        ((ForStatement) term).getForWhileStatement().addToBlock(tokens);
                    }
                    else
                        throw new VI_Exception("for block : mismatch in counting brackets");
                }
                else
                {
                    ((ForStatement) term).getForWhileStatement().addToBlock(tokens);
                }
            }

        }

        return position;
    }

    public Term getTerm() { return term; }
}


class CorrectSequence
{

    private final TokenType[] plus_minus_BinaryOperands = { TokenType.PLUS, TokenType.MINUS };

    private final TokenType[] multiply_divine_BinaryOperands = { TokenType.MULTIPLY, TokenType.DIVIDE, TokenType.PERCENT };

    private final TokenType[] booleanOperands = {TokenType.POSITIVE_EQUAL, TokenType.NEGATIVE_EQUAL,
            TokenType.MORE, TokenType.LESS,
            TokenType.MORE_EQUAL, TokenType.LESS_EQUAL,
            TokenType.AND, TokenType.OR};

    private final TokenType[] objects = { TokenType.ARRAY, TokenType.LIST, TokenType.HASH };



    CorrectSequence() { }

    public boolean checkSequence(TokenType currentType, TokenType nextType)
    {

        if(currentType == TokenType.ID )
            return IdVerification(nextType);
        else if(currentType == TokenType.NUMBER)
            return NumberVerification(nextType);
        else if(currentType == TokenType.BOOLEAN)
            return BooleanVerification(nextType);
        else if(checkFrom_plus_minus_BinaryOperands(currentType))
            return BinaryOperandVerification(nextType);
        else if(checkFrom_multiply_divine_BinaryOperands(currentType))
            return BinaryOperandVerification(nextType);
        else if(currentType == TokenType.LEFT_ROUNDED_BRACKET)
            return LeftRoundedBracketVerification(nextType);
        else if(currentType == TokenType.RIGHT_ROUNDED_BRACKET)
            return RightRoundedBracketVerification(nextType);
        else if(checkFromBooleanOperands(currentType))
            return BooleanOperandsVerification(nextType);
        else if(checkFromObjects(currentType))
            return ObjectsVerification(nextType);
        else if(currentType == TokenType.EQUAL_MARK)
            return EqualMarkVerification(nextType);
        else if(currentType == TokenType.EXCLAMATION_MARK)
            return ExclamationMarkVerification(nextType);
        else if(currentType == TokenType.TILDE)
            return TildeVerification(nextType);
        else if(currentType == TokenType.COLON)
            return ColonVerification(nextType);
        else if(currentType == TokenType.COMMA)
            return CommaVerification(nextType);
        else if(currentType == TokenType.FUNCTION)
            return FunctionVerification(nextType);
        else if(currentType == TokenType.ELSE_STATEMENT)
            return nextType == TokenType.LEFT_BRACKET;
        else if(currentType == TokenType.LEFT_BRACKET)
            return true;
        else return currentType == TokenType.RIGHT_BRACKET;
    }


    public boolean checkLastSequencePart(TokenType currentType)
    {
        return currentType == TokenType.ID
                || currentType == TokenType.NUMBER
                || currentType == TokenType.BOOLEAN
                || currentType == TokenType.RIGHT_ROUNDED_BRACKET
                || checkFromObjects(currentType);
    }


    private boolean CommaVerification(TokenType nextType)
    {
        return nextType == TokenType.ID
                || nextType == TokenType.NUMBER
                || nextType == TokenType.LEFT_ROUNDED_BRACKET
                || checkFrom_plus_minus_BinaryOperands(nextType);
    }

    private boolean FunctionVerification(TokenType nextType)
    {
        return nextType == TokenType.LEFT_ROUNDED_BRACKET;
    }

    private boolean ColonVerification(TokenType nextType)
    {
        return nextType == TokenType.FUNCTION;
    }


    private boolean BooleanOperandsVerification(TokenType nextType)
    {
        return  checkFrom_plus_minus_BinaryOperands(nextType)
                || nextType == TokenType.ID
                || nextType == TokenType.NUMBER
                || nextType == TokenType.BOOLEAN
                ||  nextType == TokenType.LEFT_ROUNDED_BRACKET;
    }

    private boolean ExclamationMarkVerification(TokenType nextType)
    {
        return  checkFrom_plus_minus_BinaryOperands(nextType)
                || nextType == TokenType.ID
                || nextType == TokenType.BOOLEAN
                || nextType == TokenType.LEFT_ROUNDED_BRACKET;
    }

    private boolean EqualMarkVerification(TokenType nextType)
    {
        return  checkFrom_plus_minus_BinaryOperands(nextType)
                || nextType == TokenType.ID
                || nextType == TokenType.NUMBER
                || nextType == TokenType.BOOLEAN
                || nextType == TokenType.LEFT_ROUNDED_BRACKET
                || nextType == TokenType.EXCLAMATION_MARK;
    }

    private boolean ObjectsVerification(TokenType nextType)
    {
        return nextType == TokenType.COMMA;
    }

    private boolean TildeVerification(TokenType nextType)
    {
        return nextType == TokenType.ARRAY
                || nextType == TokenType.LIST
                || nextType == TokenType.HASH;
    }


    private boolean RightRoundedBracketVerification(TokenType nextType)
    {
        return checkFrom_plus_minus_BinaryOperands(nextType)
                || checkFromBooleanOperands(nextType)
                || checkFrom_multiply_divine_BinaryOperands(nextType)
                || nextType == TokenType.RIGHT_ROUNDED_BRACKET
                || nextType == TokenType.LEFT_BRACKET
                || nextType == TokenType.COLON
                || nextType == TokenType.COMMA;
    }

    private boolean LeftRoundedBracketVerification(TokenType nextType)
    {
        return checkFrom_plus_minus_BinaryOperands(nextType)
                || nextType == TokenType.LEFT_ROUNDED_BRACKET
                || nextType == TokenType.RIGHT_ROUNDED_BRACKET
                || nextType == TokenType.EXCLAMATION_MARK
                || nextType == TokenType.ID
                || nextType == TokenType.NUMBER
                || nextType == TokenType.BOOLEAN;
    }

    private boolean BinaryOperandVerification(TokenType nextType)
    {
        return checkFrom_plus_minus_BinaryOperands(nextType)
                || nextType == TokenType.LEFT_ROUNDED_BRACKET
                || nextType == TokenType.ID
                || nextType == TokenType.NUMBER;

    }

    private boolean BooleanVerification(TokenType nextType)
    {
        return checkFromBooleanOperands(nextType)
                || nextType == TokenType.RIGHT_ROUNDED_BRACKET;
    }

    private boolean IdVerification(TokenType nextType)
    {
        return checkFromBooleanOperands(nextType)
                || checkFrom_plus_minus_BinaryOperands(nextType)
                || checkFrom_multiply_divine_BinaryOperands(nextType)
                ||nextType == TokenType.RIGHT_ROUNDED_BRACKET
                || nextType == TokenType.COLON
                || nextType == TokenType.TILDE
                || nextType == TokenType.EQUAL_MARK
                || nextType == TokenType.COMMA;
    }

    private boolean NumberVerification(TokenType nextType)
    {
        return checkFromBooleanOperands(nextType)
                || checkFrom_plus_minus_BinaryOperands(nextType)
                || checkFrom_multiply_divine_BinaryOperands(nextType)
                || nextType == TokenType.RIGHT_ROUNDED_BRACKET
                || nextType == TokenType.COMMA;
    }

    private boolean checkFrom_plus_minus_BinaryOperands(TokenType type)
    {
        for(TokenType baseType : plus_minus_BinaryOperands)
            if(baseType == type)
                return true;

        return false;
    }

    private boolean checkFrom_multiply_divine_BinaryOperands(TokenType type)
    {
        for(TokenType baseType : multiply_divine_BinaryOperands)
            if(baseType == type)
                return true;

        return false;
    }

    private boolean checkFromBooleanOperands(TokenType type)
    {
        for(TokenType baseType : booleanOperands)
            if(baseType == type)
                return true;

        return false;
    }

    private boolean checkFromObjects(TokenType type)
    {
        for(TokenType baseType : objects)
            if(baseType == type)
                return true;

        return false;
    }
}
