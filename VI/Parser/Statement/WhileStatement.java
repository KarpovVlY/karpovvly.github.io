package com.company.Parser.Statement;



import com.company.Lexer.Service.Token;
import com.company.Parser.Declaration.VariableDeclaration;
import com.company.Parser.Parser;
import com.company.Parser.Service.Expression;
import com.company.Parser.Service.Term;
import com.company.VI_Exception;

import java.util.ArrayList;

public class WhileStatement extends Term
{
    private Expression paramsExpression = null;
    private ArrayList<Term> blockTerms = null;

    private ArrayList<ArrayList<Token>> block = null;

    public WhileStatement() { }


    @Override
    public void process() throws VI_Exception
    {
        paramsExpression = super.processTokens(0, null);
        tokens = new ArrayList<>();
    }

    public void processBlock() throws VI_Exception
    {
        blockTerms = new ArrayList<>();

        int position = 0;
        Parser parser;

        for (; position < block.size(); position++)
        {
            ArrayList<Token> tokens = block.get(position);

            parser = new Parser(tokens);

            if (!parser.blockFinished)
            {
                while(!parser.blockFinished)
                {
                    ++position;
                    if(position == block.size())
                    {
                        break;
                    }
                    tokens = block.get(position);
                    position = parser.fillBlock(tokens, position);

                }

            }

            blockTerms.add(parser.getTerm());
        }

        tokens = null;
        block = null;
    }



    @Override
    public WhileStatement clone()
    {
        WhileStatement clone = new WhileStatement();

        clone.paramsExpression = new Expression();

        for(Object o : paramsExpression.getExpressionObjects()) {
            try { clone.paramsExpression.add(o); }
            catch (VI_Exception e) { e.printStackTrace(); }
        }


        clone.blockTerms = new ArrayList<>();

        for(Term t : blockTerms)
            clone.blockTerms.add(t.clone());

        return clone;
    }




    public ArrayList<ArrayList<Token>> getBlock() { return block; }
    public void initBlock() { block = new ArrayList<>(); }
    public void addToBlock(ArrayList<Token> tokens) { block.add(tokens); }

    public ArrayList<Term> getBlockTerms() { return blockTerms; }
    public Expression getParamsExpression() { return paramsExpression;}


}
