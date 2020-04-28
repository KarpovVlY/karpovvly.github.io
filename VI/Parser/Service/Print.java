package com.company.Parser.Service;


import com.company.VI_Exception;

import java.util.ArrayList;

public class Print extends Term
{
    protected Expression printExpression = null;

    @Override
    public void process() throws VI_Exception
    {
        printExpression = super.processTokens(0, null);
        tokens = new ArrayList<>();
    }

    @Override
    public Term clone() {
        return super.clone();
    }

    public Expression getPrintExpression() { return printExpression; }
    protected void setPrintExpression(Expression printExpression) { this.printExpression = printExpression; }


}
