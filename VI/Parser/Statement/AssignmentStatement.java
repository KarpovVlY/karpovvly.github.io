package com.company.Parser.Statement;


import com.company.Lexer.Service.Token;
import com.company.Lexer.Service.TokenType;
import com.company.Parser.Expression.AddictiveExpression;
import com.company.Parser.Expression.TypeExpression;
import com.company.Parser.Service.Expression;
import com.company.Parser.Service.Term;
import com.company.VI_Exception;


public class AssignmentStatement extends Term
{
    private Token ID;
    private Expression expression = new Expression();


    public AssignmentStatement(Token currentToken) { this.ID = currentToken; }


    @Override
    public void process() throws VI_Exception
    {
        Expression currentExpression = null;

        Token currentToken = tokens.get(0);

        if(currentToken.getTokenType() == TokenType.COLON)
        {
            currentExpression = new AddictiveExpression(ID);
            currentExpression.add(currentToken);
            ID = null;
        }
        else if(currentToken.getTokenType() == TokenType.TILDE)
            currentExpression = new TypeExpression(currentToken);
        else if(currentToken.getTokenType() != TokenType.EQUAL_MARK)
            throw new VI_Exception(currentToken.getContent() + " : missed assignment or call sign");



        for(Object o : super.processTokens(1, currentExpression).getExpressionObjects())
            expression.add(o);


        tokens = null;
    }



    public AssignmentStatement clone()
    {
       try
       {
           AssignmentStatement ass = new AssignmentStatement(ID);

           Expression expression = new Expression();

           for(Object o : this.expression.getExpressionObjects())
               try
               {
                   if(o instanceof AddictiveExpression)
                       expression.add((((AddictiveExpression) o).clone()));
                   else
                       expression.add(o);
               }
               catch (VI_Exception e) { e.printStackTrace(); }

           ass.setExpression(expression);

           return ass;
       }
       catch (Exception e)
       {
           System.err.println("unexpected error in statement");
           return null;
       }
    }


    public Token getID() { return ID; }
    public Expression getExpression() { return expression; }
    public void setExpression(Expression expression) { this.expression = expression; }
}
