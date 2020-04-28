package com.company.Parser.Statement;

import com.company.Parser.Expression.AddictiveExpression;
import com.company.Parser.Service.Expression;
import com.company.Parser.Service.Print;

public class PrintlnStatement extends Print
{
    public PrintlnStatement(){}


    @Override
    public PrintlnStatement clone()
    {

        PrintlnStatement clone = new PrintlnStatement();

        Expression expression = new Expression();

        for(Object o : this.printExpression.getExpressionObjects())

            try
            {
                if(o instanceof AddictiveExpression)
                    expression.add((((AddictiveExpression) o).clone()));
                else
                    expression.add(o);
            }
            catch (Exception e) { e.printStackTrace(); }


        clone.setPrintExpression(expression);

        return clone;
    }
}
