package com.company.Interpreter;

import com.company.Interpreter.Service.BinaryCalculation;
import com.company.Interpreter.Service.Variable;
import com.company.Interpreter.Value.ArrayValue.ArrayValue;
import com.company.Interpreter.Value.DoubleConnectedListValue.VI_DoubleConnectedList;
import com.company.Interpreter.Value.HashTableValue.VI_HashTable;
import com.company.Interpreter.Value.SingleValue.SingleValue;
import com.company.Interpreter.Value.VariableValue;
import com.company.Lexer.Service.Token;
import com.company.Lexer.Service.TokenType;
import com.company.Parser.Declaration.VariableDeclaration;
import com.company.Parser.Declaration.VariableDeclarator;
import com.company.Parser.Expression.AddictiveExpression;
import com.company.Parser.Expression.TypeExpression;
import com.company.Parser.Service.Expression;
import com.company.Parser.Service.Term;
import com.company.Parser.Statement.*;
import com.company.VI_Exception;

import java.util.ArrayList;

public class Interpreter
{

    private final ArrayList<Variable> variables = new ArrayList<>();

    private Interpreter parentInterpreter = null;


    public Interpreter() { }
    public Interpreter(Interpreter parentInterpreter) { this.parentInterpreter = parentInterpreter; }


    /**
     *  execute processing
     */
    public void execute(Term term) throws VI_Exception
    {
        if(term instanceof VariableDeclaration)
        {
            executeVariablesDeclaration((VariableDeclaration) term);
        }
        else if(term instanceof AssignmentStatement)
        {
            executeAssignmentStatement((AssignmentStatement) term);
        }
        else if(term instanceof IfStatement)
        {
            executeIfStatement((IfStatement) term);
        }
        else if(term instanceof WhileStatement)
        {
            executeWhileStatement((WhileStatement) term);
        }
        else if(term instanceof ForStatement)
        {
            executeForStatement((ForStatement) term);
        }
        else if(term instanceof PrintStatement)
        {
            executePrintStatement((PrintStatement) term);
        }
        else if(term instanceof PrintlnStatement)
        {
            executePrintlnStatement((PrintlnStatement) term);
        }
    }


    /**
     *  Variable processing
     */
    private void executeVariablesDeclaration(VariableDeclaration variableDeclaration) throws VI_Exception
    {
        for(VariableDeclarator variableDeclarator : variableDeclaration.getVariablesDeclarators())
        {

            for(Variable var : variables)
                if(var.getVariableName().compareTo(variableDeclarator.getID().getContent()) == 0)
                    throw new VI_Exception(var.getVariableName() + " : variable already exists");


            Object result = processExpression(variableDeclarator.getExpression());

            if(result == null)
            {
                addVariable(variableDeclarator.getID().getContent());
            }
            else if(result instanceof Token)
            {
                addVariable(variableDeclarator.getID().getContent(), (Token)result);
            }
            else if(result instanceof ArrayValue)
            {
                addVariable(variableDeclarator.getID().getContent(), (ArrayValue)result);
            }
            else if(result instanceof VI_HashTable)
            {
                addVariable(variableDeclarator.getID().getContent(), (VI_HashTable<Object, Object>)result);
            }
            else if(result instanceof VI_DoubleConnectedList)
            {
                addVariable(variableDeclarator.getID().getContent(), (VI_DoubleConnectedList<Object>) result);
            }

        }
    }


    /**
     *  Assignment processing
     */
    private void executeAssignmentStatement(AssignmentStatement assignmentStatement) throws VI_Exception {

        if(assignmentStatement.getID() != null )
        {
            String ID = assignmentStatement.getID().getContent();
            Interpreter currentInterpreter = this;

            int pos = getVariablePosition(ID, this);

            if(pos == -1)
                while(currentInterpreter.parentInterpreter != null)
                    if(pos == -1)
                    {
                        currentInterpreter = currentInterpreter.parentInterpreter;
                        pos = getVariablePosition(ID, currentInterpreter);
                    }
                    else
                        break;


            if(pos == -1)
                throw new VI_Exception(ID + " : variable doesn't exist");

            Object result = processExpression(assignmentStatement.getExpression());


            if(result instanceof Token)
            {
                currentInterpreter.setVariable(ID, (Token) result, pos);
            }
            else if(result instanceof ArrayValue)
            {
                currentInterpreter.setVariable(ID, (ArrayValue) result, pos);
            }
            else if(result instanceof VI_HashTable)
            {
                currentInterpreter.setVariable(ID, (VI_HashTable<Object, Object>) result, pos);
            }
            else if(result instanceof VI_DoubleConnectedList)
            {
                currentInterpreter.setVariable(ID, (VI_DoubleConnectedList<Object>) result, pos);
            }
            else if(result == null)
                throw new VI_Exception(ID + " : incorrect assignment");
        }
        else
        {
            processExpression(assignmentStatement.getExpression());
        }
    }


    /**
     *  If processing
     */
    private void executeIfStatement(IfStatement ifStatement) throws VI_Exception
    {
        Object result = processExpression(ifStatement.paramsExpression);

        assert result != null;
        if(((Token)result).getTokenType() != TokenType.BOOLEAN)
            throw new VI_Exception("invalid type of expression");
        else
        {
            Interpreter localInterpreter = new Interpreter(this);

            if(((Token) result).getContent().compareTo("true") == 0)
                for(Term term : ifStatement.getPositiveTerms())
                    localInterpreter.execute(term);
            else
            if(ifStatement.getNegativeTerms() != null)
                for(Term term : ifStatement.getNegativeTerms())
                    localInterpreter.execute(term);
        }
    }


    /**
     *  While processing
     */
    private void executeWhileStatement(WhileStatement whileStatement) throws VI_Exception
    {
        WhileStatement clone = whileStatement.clone();

        Object result = processExpression(clone.getParamsExpression());

        assert result != null;
        if(((Token)result).getTokenType() != TokenType.BOOLEAN)
            throw new VI_Exception("invalid type of expression");
        else
        {
            while(((Token)result).getContent().compareTo("true") == 0)
            {
                clone = whileStatement.clone();

                Interpreter localInterpreter = new Interpreter(this);
                for(Term term : clone.getBlockTerms())
                    localInterpreter.execute(term);
                result = processExpression(clone.getParamsExpression());
            }
        }
    }


    /**
     *  For processing
     */
    private void executeForStatement(ForStatement forStatement) throws VI_Exception
    {
        this.execute(forStatement.getForVariableAssignment());

        WhileStatement whileClone = forStatement.getForWhileStatement().clone();
        AssignmentStatement assignmentClone = forStatement.getForAssignmentStatement().clone();

        Object result = processExpression(whileClone.getParamsExpression());

        assert result != null;
        if(((Token)result).getTokenType() != TokenType.BOOLEAN)
            throw new VI_Exception(((Token)result).getTokenType() + " : invalid type of expression, expected BOOLEAN");
        else
        {
            while(((Token)result).getContent().compareTo("true") == 0)
            {
                whileClone = forStatement.getForWhileStatement().clone();

                Interpreter localInterpreter = new Interpreter(this);
                for(Term term : whileClone.getBlockTerms())
                    localInterpreter.execute(term);


                this.execute(assignmentClone);
                assignmentClone = forStatement.getForAssignmentStatement().clone();

                result = processExpression(whileClone.getParamsExpression());
            }
        }

        Token ID;

        if(forStatement.getForVariableAssignment() instanceof AssignmentStatement)
            ID = ((AssignmentStatement) forStatement.getForVariableAssignment()).getID();
        else if(forStatement.getForVariableAssignment() instanceof VariableDeclaration)
            ID = ((VariableDeclaration) forStatement.getForVariableAssignment()).getVariablesDeclarators().get(0).getID();
        else
            throw new VI_Exception("unexpected error in for params");

        this.deleteVariable(ID);
    }


    /**
     *  Print processing
     */

    private void executePrintStatement(PrintStatement printStatement) throws VI_Exception
    {
        Object result = processExpression(printStatement.getPrintExpression());

        if(result != null)
            if(((Token)result).getContent() == null)
                System.out.print("  ");
            else
                System.out.print(((Token)result).getContent());
        else
            throw new VI_Exception("incorrect params in print() ");
    }


    private void executePrintlnStatement(PrintlnStatement printlnStatement) throws VI_Exception
    {
        Object result = processExpression(printlnStatement.getPrintExpression());

        if(result != null)
            if(((Token)result).getContent() == null)
                System.out.println();
            else
                System.out.println(((Token)result).getContent());
        else
            throw new VI_Exception("incorrect params in println() ");
    }


    /**
     *
     *  Service functions
     *
     */
    private Object processExpression(Expression expression) throws VI_Exception
    {
        ArrayList<Object> expressionObjects = expression.getExpressionObjects();

        if(expressionObjects.size() == 0)
            return new Token(null,null);

        Object result;
        Object currentObject;

        for(int i = 0 ; i < expressionObjects.size() ; i ++)
        {
            currentObject = expressionObjects.get(i);

            if(currentObject instanceof AddictiveExpression)
            {
                result = processAddictive((AddictiveExpression) currentObject);


               if(result instanceof SingleValue)
                    expressionObjects.set(i, ((SingleValue) result).getSingleValue());
                else if(result instanceof ArrayValue)
                    return result;

                else if(result instanceof Token)
                {
                    if(((Token) result).getContent() != null)
                        expressionObjects.set(i, result);
                    else
                        return result;
                }
                else if(result instanceof Variable)
                    return result;
                else if(result == null)
                    return null;

            }
            else if(currentObject instanceof TypeExpression)
            {
                return processType((TypeExpression)currentObject);
            }
        }

        ArrayList<Token> binaryResult = new ArrayList<>();

        for(Object o : expressionObjects)
            binaryResult.add((Token)o);

        return new BinaryCalculation(binaryResult).getResult();
    }


    private Object processType(TypeExpression typeExpression)
    {
        if(typeExpression.getTypeExpression() == TokenType.HASH)
            return new VI_HashTable<>();
        else  if(typeExpression.getTypeExpression() == TokenType.LIST)
            return new VI_DoubleConnectedList<>();
        else  if(typeExpression.getTypeExpression() == TokenType.ARRAY)
            return new ArrayValue();

        return null;
    }


    private Object processAddictive(AddictiveExpression addictiveExpression) throws VI_Exception
    {

        Object currentObject;

        for(int i = 0 ; i < addictiveExpression.getExpressionObjects().size() ; i ++)
        {
            currentObject = addictiveExpression.getExpressionObjects().get(i);

            if(currentObject instanceof AddictiveExpression)
            {
                Object res = processAddictive((AddictiveExpression)currentObject);
                addictiveExpression.getExpressionObjects().set(i, res);
            }
        }

        String ID = addictiveExpression.getID().getContent();
        Variable var = getVariable(ID, this);
        Interpreter currentInterpreter = this;



        if(var == null)
        {
            while(currentInterpreter.parentInterpreter != null)
            {
                if(var == null)
                {
                    currentInterpreter = currentInterpreter.parentInterpreter;
                    var = getVariable(ID, currentInterpreter);
                }
                else
                    break;
            }
        }

        if(var != null)
            return addictiveExpression.processValue(var);
        else
            throw new VI_Exception(ID + " : variable doesn't exist");
    }


    /**
     *
     *  Variables processing
     *
     */

    private void addVariable(String ID) {variables.add(new Variable(ID, new VariableValue())); }

    private void addVariable(String ID, Token value) { variables.add(new Variable(ID, new SingleValue(value))); }

    private void addVariable(String ID, ArrayValue arrayValue) { variables.add(new Variable(ID, arrayValue)); }

    private void addVariable(String ID, VI_HashTable<Object, Object> hashValue) { variables.add(new Variable(ID, hashValue)); }

    private void addVariable(String ID, VI_DoubleConnectedList<Object> listValue) { variables.add(new Variable(ID, listValue)); }



    private void setVariable(String ID, Token token,  int pos) { variables.set(pos, new Variable(ID, new SingleValue(token))); }

    private void setVariable(String ID, VariableValue arrayValue, int pos) { variables.set(pos, new Variable(ID, arrayValue)); }

    private void setVariable(String ID, VI_HashTable<Object, Object> hashValue, int pos) { variables.set(pos, new Variable(ID, hashValue)); }

    private void setVariable(String ID, VI_DoubleConnectedList<Object> listValue, int pos) { variables.set(pos, new Variable(ID, listValue)); }



    private Variable getVariable(String ID, Interpreter interpreter)
    {
        for(Variable var : interpreter.variables)
            if(var.getVariableName().compareTo(ID) == 0)
                return var;

        return null;
    }

    private int getVariablePosition(String ID, Interpreter interpreter)
    {
        int pos = -1;

        for(int i = 0 ; i < interpreter.variables.size() ; i ++)
            if(ID.compareTo(interpreter.variables.get(i).getVariableName()) == 0)
            {
                pos = i;
                break;
            }

        return pos;
    }

    private void deleteVariable(Token ID)
    {
        variables.removeIf(var -> var.getVariableName().compareTo(ID.getContent()) == 0);
    }
}