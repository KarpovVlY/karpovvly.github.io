package com.company.Parser.Declaration;

import com.company.Interpreter.Service.Variable;
import com.company.Lexer.Service.Token;
import com.company.Lexer.Service.TokenType;
import com.company.Parser.Service.Expression;
import com.company.Parser.Service.Term;
import com.company.VI_Exception;

import java.util.ArrayList;

public class VariableDeclaration extends Term
{

    private ArrayList<VariableDeclarator> variablesDeclarators = new ArrayList<>();

    public VariableDeclaration() { }

    @Override
    public void process() throws VI_Exception
    {
        ArrayList<Token> currentTokens = new ArrayList<>();
        boolean functionCall = false;

        for(Token currentToken : tokens)
        {
            if(currentToken.getTokenType() == TokenType.RIGHT_ROUNDED_BRACKET)
            {
                currentTokens.add(currentToken);
                functionCall = false;
            }
            else if(currentToken.getTokenType() == TokenType.COLON)
            {
                currentTokens.add(currentToken);
                functionCall = true;
            }
            else if(currentToken.getTokenType() == TokenType.COMMA )
            {
                if(!functionCall)
                {
                    variablesDeclarators.add(new VariableDeclarator(currentTokens, this));
                    currentTokens = new ArrayList<>();
                }
                else
                {
                    currentTokens.add(currentToken);
                }
            }
            else
            {
                currentTokens.add(currentToken);
            }
        }

        variablesDeclarators.add(new VariableDeclarator(currentTokens, this));
        tokens = null;
    }



    public Expression call(int position, Expression current) throws VI_Exception { return super.processTokens(position,current); }

    @Override
    public VariableDeclaration clone()
    {
        VariableDeclaration clone = new VariableDeclaration();

        clone.variablesDeclarators = new ArrayList<>();

        for(VariableDeclarator vd  : this.getVariablesDeclarators())
            clone.variablesDeclarators.add(vd.clone());

        return clone;
    }

    public ArrayList<VariableDeclarator> getVariablesDeclarators() { return variablesDeclarators; }
}
