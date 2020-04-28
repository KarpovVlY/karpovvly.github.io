package com.company.Parser.Statement;



import com.company.Lexer.Service.Token;
import com.company.Parser.Expression.AddictiveExpression;
import com.company.Parser.Parser;
import com.company.Parser.Service.Expression;
import com.company.Parser.Service.Term;
import com.company.VI_Exception;

import java.util.ArrayList;

public class IfStatement extends Term
{
    public Expression paramsExpression = null;
    private ArrayList<Term> positiveTerms = null;
    private ArrayList<Term> negativeTerms = null;

    private ArrayList<ArrayList<Token>> block = null;


    public IfStatement() { }


    @Override
    public void process() throws VI_Exception
    {
        paramsExpression = super.processTokens(0, null);
        tokens = new ArrayList<>();
    }


    public void processBlock() throws VI_Exception
    {
        ArrayList<Term> currentTerms;

        if(positiveTerms == null)
        {
            positiveTerms = new ArrayList<>();
            currentTerms = positiveTerms;
        }
        else if(negativeTerms == null)
        {
            negativeTerms = new ArrayList<>();
            currentTerms = negativeTerms;
        }
        else
            throw new VI_Exception("Error in processing if block");


        Parser parser;

        for (int position = 0; position < block.size(); position++)
        {
            ArrayList<Token> tokens = block.get(position);

            parser = new Parser(tokens);

            if (!parser.blockFinished)
            {
                while(!parser.blockFinished)
                {
                    ++position;
                    tokens = block.get(position);
                    position = parser.fillBlock(tokens, position);

                }
            }

            currentTerms.add(parser.getTerm());
        }

        tokens = null;
        block = null;
    }



    @Override
    public IfStatement clone()
    {
        IfStatement clone = new IfStatement();

        clone.paramsExpression = new Expression();

        for(Object o : paramsExpression.getExpressionObjects())
        {
            try
            {
                if(o instanceof AddictiveExpression)
                    clone.paramsExpression.add(((AddictiveExpression) o).clone());
                else
                    clone.paramsExpression.add(o);
            }
            catch (VI_Exception e) { e.printStackTrace(); }
        }

        if(this.positiveTerms != null)
        {
            clone.positiveTerms = new ArrayList<>();
            for (Term t : this.positiveTerms)
                clone.positiveTerms.add(t.clone());
        }

        if(this.negativeTerms != null)
        {
            clone.negativeTerms = new ArrayList<>();
            for(Term t : this.negativeTerms)
                clone.negativeTerms.add(t.clone());
        }


        return clone;
    }

    public ArrayList<ArrayList<Token>> getBlock() { return block; }
    public void initBlock() { block = new ArrayList<>(); }
    public void addToBlock(ArrayList<Token> tokens) { block.add(tokens); }


    public ArrayList<Term> getPositiveTerms() { return positiveTerms; }
    public ArrayList<Term> getNegativeTerms() { return negativeTerms; }


    public boolean isFullPositive() { return positiveTerms != null; }
    public boolean isFullNegative() { return negativeTerms == null; }
}
