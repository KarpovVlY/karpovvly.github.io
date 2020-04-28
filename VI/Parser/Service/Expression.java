package com.company.Parser.Service;

import com.company.VI_Exception;

import java.util.ArrayList;

public class Expression
{

    public ArrayList<Object> getExpressionObjects() { return expressionObjects; }

    protected ArrayList<Object> expressionObjects = new ArrayList<>();


    public Expression(){}


    public void add(Object o) throws VI_Exception { expressionObjects.add(o); }

    public void process() throws VI_Exception { }

}
