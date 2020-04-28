package com.company.Parser.Statement;


import com.company.Interpreter.Service.Variable;
import com.company.Lexer.Service.Token;
import com.company.Lexer.Service.TokenType;
import com.company.Parser.Declaration.VariableDeclaration;
import com.company.Parser.Declaration.VariableDeclarator;
import com.company.Parser.Parser;
import com.company.Parser.Service.Term;
import com.company.VI_Exception;

import java.util.ArrayList;

public class ForStatement extends Term
{
    private Term forVariableAssignment = null;


    private WhileStatement forWhileStatement = null;
    private AssignmentStatement forAssignmentStatement = null;


    public ForStatement() { }


    @Override
    public void process() throws VI_Exception { convertParams(); }


    private void convertParams() throws VI_Exception
    {

        ArrayList<Token> buffer = new ArrayList<>();

        for(Token currentToken : tokens)
        {
            if(currentToken.getTokenType() != TokenType.COMMA)
            {
                buffer.add(currentToken);
            }
            else
            {
                createCompositeStatement(buffer);
                buffer = new ArrayList<>();
            }
        }

        createCompositeStatement(buffer);
    }

    private void createCompositeStatement(ArrayList<Token> buffer) throws VI_Exception
    {

        if(forVariableAssignment == null)
        {
            forVariableAssignment = new Parser(buffer).getTerm();
        }
        else if(forWhileStatement == null)
        {
            forWhileStatement = new WhileStatement();
            for(Token token : buffer)
                forWhileStatement.add(token);
            forWhileStatement.process();

        }
        else if(forAssignmentStatement == null)
        {
           forAssignmentStatement = (AssignmentStatement) new Parser(buffer).getTerm();
        }
        else
            throw new VI_Exception("System error in processing for params");

    }




    public Term getForVariableAssignment() { return forVariableAssignment; }
    public AssignmentStatement getForAssignmentStatement() { return forAssignmentStatement; }
    public WhileStatement getForWhileStatement() { return forWhileStatement; }
}
