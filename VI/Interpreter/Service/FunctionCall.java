package com.company.Interpreter.Service;


import com.company.Interpreter.Value.ArrayValue.ArrayValue;
import com.company.Interpreter.Value.DoubleConnectedListValue.VI_DoubleConnectedList;
import com.company.Interpreter.Value.HashTableValue.VI_HashTable;
import com.company.Lexer.Service.Token;
import com.company.Lexer.Service.TokenType;
import com.company.VI_Exception;

import java.util.ArrayList;
import java.util.Objects;

public class FunctionCall
{
    public void setFunctionCall(Token functionCall) { this.functionCall = functionCall; }
    public Token getFunctionCall() { return functionCall; }

    private Token functionCall = null;

    private ArrayList<Object> functionArguments = new ArrayList<>();
    private Variable variable;
    public boolean argumentsStarted = false;


    public FunctionCall() { }

    public void add(Object token) { functionArguments.add(token); }

    /**
     *  Function processing
     */
    public Object processFunctionCall(Variable variable) throws VI_Exception
    {
        this.variable = variable;



        if(functionArguments.size() != 0)
            split();

        if(variable.getVariableType()== TokenType.LIST)
            return processVI_DoubleConnectedList();
        else if(variable.getVariableType() == TokenType.HASH)
            return processVI_HashTable();
        else if(variable.getVariableType() == TokenType.ARRAY)
            return processArray();

        throw new VI_Exception("unknown object type");
    }

    /**
     *  Array processing
     */
    private Object processArray() throws VI_Exception
    {
        String functionName = functionCall.getContent();

        if(functionName.compareTo("add") == 0)
        {
            if(functionArguments.size() == 1)
            {
                ((ArrayValue) variable.getVariableValue())
                        .add(functionArguments.get(0));
                return null;
            }
            else
                throw new VI_Exception("error in calling add(), incorrect arguments");
        }
        else if(functionName.compareTo("get") == 0)
        {
            if(functionArguments.size() == 1)
                return ((ArrayValue) variable.getVariableValue())
                        .get(convert(((Token)functionArguments.get(0)).getContent()));
            else
                throw new VI_Exception("error in calling get(), incorrect arguments");
        }
        else if(functionName.compareTo("set") == 0)
        {
            if(functionArguments.size() == 2)
            {

                ((ArrayValue) variable.getVariableValue())
                        .set(functionArguments.get(0), convert(((Token)functionArguments.get(1)).getContent()));
                return null;
            }
            else
                throw new VI_Exception("error in calling set(), incorrect arguments");
        }
        else if(functionName.compareTo("remove") == 0)
        {
            if(functionArguments.size() == 1)
            {
                ((ArrayValue) variable.getVariableValue())
                        .remove(convert(((Token)functionArguments.get(0)).getContent()));
                return null;
            }
            else
                throw new VI_Exception("error in calling remove(), incorrect arguments");
        }
        else if(functionName.compareTo("setSize") == 0)
        {
            if(functionArguments.size() == 1)
            {
                ((ArrayValue) variable.getVariableValue())
                        .setSize(convert(((Token)functionArguments.get(0)).getContent()));
                return null;
            }
            else
                throw new VI_Exception("error in calling setSize(), incorrect arguments");
        }
        else if(functionName.compareTo("getSize") == 0)
        {
            if(functionArguments.size() == 0)
                return new Token(Integer.toString(((ArrayValue) variable.getVariableValue())
                        .getSize()),TokenType.NUMBER);
            else
                throw new VI_Exception("error in calling getSize(), incorrect arguments");
        }
        else if(functionName.compareTo("clear") == 0)
        {
            if(functionArguments.size() == 0)
            {
                ((ArrayValue) variable.getVariableValue())
                        .clear();
                return null;
            }
            else
                throw new VI_Exception("error in calling clear(), incorrect arguments");
        }
        else if(functionName.compareTo("isEmpty") == 0)
        {
            if(functionArguments.size() == 0)
                return ((ArrayValue) variable.getVariableValue())
                        .isEmpty();
            else
                throw new VI_Exception("error in calling isEmpty(), incorrect arguments");
        }

        throw new VI_Exception(functionName + " : error in calling function from Array");
    }


    /**
     *  Hash processing
     */
    private Object processVI_HashTable() throws VI_Exception
    {
        String functionName = functionCall.getContent();

        if(functionName.compareTo("put") == 0)
        {
            if(functionArguments.size() == 2)
            {

                ((VI_HashTable<Object, Object>)variable.getVariableValue())
                        .put(((Token)functionArguments.get(0)).getContent(), functionArguments.get(1));
                return null;
            }
            else
                throw new VI_Exception("Error in calling put(), incorrect arguments");
        }
        else if(functionName.compareTo("get") == 0)
        {
            if(functionArguments.size() == 1)
            {
                Object o = ((VI_HashTable<Object, Object>) variable.getVariableValue())
                        .get(((Token) functionArguments.get(0)).getContent());

                return Objects.requireNonNullElseGet(o, () -> new Token(null, null));
            }
            else
                throw new VI_Exception("Error in calling get(), incorrect arguments");
        }
        if(functionName.compareTo("replace") == 0)
        {
            if(functionArguments.size() == 2)
            {
                ((VI_HashTable<Object, Object>)variable.getVariableValue())
                        .replace(((Token)functionArguments.get(0)).getContent(), functionArguments.get(1));
                return null;
            }
            else
                throw new VI_Exception("Error in calling replace(), incorrect arguments");
        }
        else if(functionName.compareTo("remove") == 0)
        {
            if(functionArguments.size() == 1)
            {
                ((VI_HashTable<Object, Object>)variable.getVariableValue())
                        .remove(((Token)functionArguments.get(0)).getContent());
                return null;
            }
            else
                throw new VI_Exception("Error in calling remove(), incorrect arguments");
        }
        else if(functionName.compareTo("containsKey") == 0)
        {
            if(functionArguments.size() == 1)
            {
                if ((((VI_HashTable<Object, Object>) variable.getVariableValue())
                        .containsKey(((Token)functionArguments.get(0)).getContent())))
                    return new Token("true", TokenType.BOOLEAN);
                else
                    return new Token("false", TokenType.BOOLEAN);
            }
            else
                throw new VI_Exception("Error in calling containsKey(), incorrect arguments");
        }
        else if(functionName.compareTo("containsValue") == 0)
        {
            if(functionArguments.size() == 1)
                if((((VI_HashTable<Object, Object>) variable.getVariableValue())
                        .containsValue(functionArguments.get(0))))
                    return new Token("true", TokenType.BOOLEAN);
                else
                    return new Token("false", TokenType.BOOLEAN);
            else
                throw new VI_Exception("Error in calling containsValue(), incorrect arguments");
        }
        else if(functionName.compareTo("getSize") == 0)
        {
            if(functionArguments.size() == 0)
                return new Token(Integer.toString(((VI_HashTable<Object, Object>) variable.getVariableValue())
                        .getSize()), TokenType.NUMBER);
            else
                throw new VI_Exception("Error in calling getSize(), incorrect arguments");
        }
        else if(functionName.compareTo("clear") == 0)
        {
            if(functionArguments.size() == 0)
            {
                ((VI_HashTable<Object, Object>)variable.getVariableValue())
                        .clear();
                return null;
            }
            else
                throw new VI_Exception("Error in calling clear(), incorrect arguments");
        }
        else if(functionName.compareTo("isEmpty") == 0)
        {
            if(functionArguments.size() == 0)
            {
                if ((((VI_HashTable<Object, Object>) variable.getVariableValue()).isEmpty()))
                    return new Token("true", TokenType.BOOLEAN);
                else
                    return new Token("false", TokenType.BOOLEAN);
            }
            else
                throw new VI_Exception("Error in calling isEmpty(), incorrect arguments");
        }


        throw new VI_Exception("Error in calling function from Hash");
    }

    /**
     *  List processing
     */
    private Object processVI_DoubleConnectedList() throws VI_Exception
    {
        String functionName = functionCall.getContent();


        if(functionName.compareTo("add") == 0)
        {
            if(functionArguments.size() != 0)
            {
                if(functionArguments.size() == 1)
                    ((VI_DoubleConnectedList<Object>)variable.getVariableValue()).add(functionArguments.get(0));
                else if(functionArguments.size() == 2)
                    ((VI_DoubleConnectedList<Object>)variable.getVariableValue()).add(functionArguments.get(0),
                            (convert(((Token)functionArguments.get(1)).getContent())));
                else
                    throw new VI_Exception("Error in calling add(), incorrect arguments");

                return null;
            }
            else
                throw new VI_Exception("Error in calling add(), incorrect arguments");
        }
        else if(functionName.compareTo("addForward") == 0)
        {
            if(functionArguments.size() != 0)
            {
                ((VI_DoubleConnectedList<Object>)variable.getVariableValue()).addForward(functionArguments.get(0));
                return null;
            }
            else
                throw new VI_Exception("Error in calling addForward(), incorrect arguments");
        }
        else if(functionName.compareTo("addBackward") == 0)
        {
            if(functionArguments.size() != 0)
            {
                ((VI_DoubleConnectedList<Object>)variable.getVariableValue()).addBackward(functionArguments.get(0));
                return null;
            }
            else
                throw new VI_Exception("Error in calling addBackward(), incorrect arguments");
        }
        else if(functionName.compareTo("get") == 0)
        {
            if(functionArguments.size() == 1)
                return ((VI_DoubleConnectedList<Object>)variable.getVariableValue())
                        .get(convert(((Token)functionArguments.get(0)).getContent()));
            else
                throw new VI_Exception("Error in calling get(), incorrect arguments");
        }
        else if(functionName.compareTo("getFirst") == 0)
        {
            if(functionArguments.size() == 0)
                return ((VI_DoubleConnectedList<Object>)variable.getVariableValue()).getFirst();
            else
                throw new VI_Exception("Error in calling getFirst(), incorrect arguments");
        }
        else if(functionName.compareTo("getLast") == 0)
        {
            if(functionArguments.size() == 0)
                return ((VI_DoubleConnectedList<Object>)variable.getVariableValue()).getLast();
            else
                throw new VI_Exception("Error in calling getLast(), incorrect arguments");
        }
        else if(functionName.compareTo("set") == 0)
        {
            if(functionArguments.size() == 2)
            {
                ((VI_DoubleConnectedList<Object>)variable.getVariableValue()).set(functionArguments.get(0), (convert(((Token)functionArguments.get(1)).getContent())));
                return null;
            }
            else
                throw new VI_Exception("Error in calling set(), incorrect arguments");
        }
        else if(functionName.compareTo("remove") == 0)
        {
            if(functionArguments.size() == 1)
            {
                ((VI_DoubleConnectedList<Object>)variable.getVariableValue()).remove((convert(((Token)functionArguments.get(0)).getContent())));
                return null;
            }
            else
                throw new VI_Exception("Error in calling remove(), incorrect arguments");
        }
        else if(functionName.compareTo("removeFirst") == 0)
        {
            if(functionArguments.size() == 0)
            {
                ((VI_DoubleConnectedList<Object>)variable.getVariableValue()).removeFirst();
                return null;
            }
            else
                throw new VI_Exception("Error in calling removeFirst(), incorrect arguments");
        }
        else if(functionName.compareTo("removeLast") == 0)
        {
            if(functionArguments.size() == 0)
            {
                ((VI_DoubleConnectedList<Object>)variable.getVariableValue()).removeLast();
                return null;
            }
            else
                throw new VI_Exception("Error in calling removeLast(), incorrect arguments");
        }
        else if(functionName.compareTo("pop") == 0)
        {
            if(functionArguments.size() == 0)
                return ((VI_DoubleConnectedList<Object>)variable.getVariableValue()).pop();
            else
                throw new VI_Exception("Error in calling pop(), incorrect arguments");
        }
        else if(functionName.compareTo("peek") == 0)
        {
            if(functionArguments.size() == 0)
                return ((VI_DoubleConnectedList<Object>)variable.getVariableValue()).peek();
            else
                throw new VI_Exception("Error in calling peek(), incorrect arguments");
        }
        if(functionName.compareTo("getSize") == 0)
        {
            if(functionArguments.size() == 0)
                return new Token(Integer.toString(((VI_DoubleConnectedList<Object>) variable.getVariableValue()).getSize()), TokenType.NUMBER);
            else
                throw new VI_Exception("Error in calling getSize(), incorrect arguments");
        }
        else if(functionName.compareTo("isEmpty") == 0)
        {
            if(functionArguments.size() == 0)
                if((((VI_DoubleConnectedList<Object>) variable.getVariableValue()).isEmpty()))
                    return new Token("true", TokenType.BOOLEAN);
                else
                    return new Token("false", TokenType.BOOLEAN);
            else
                throw new VI_Exception("Error in calling isEmpty(), incorrect arguments");

        }
        else if(functionName.compareTo("clear") == 0)
        {
            if(functionArguments.size() == 0)
            {
                ((VI_DoubleConnectedList<Object>)variable.getVariableValue()).clear();
                return null;
            }
            else
                throw new VI_Exception("Error in calling clear(), incorrect arguments");

        }

        throw new VI_Exception("Error in calling function from List");
    }



    /**
     *  Convert token to index
     */
    private int convert(String doubleValue) throws VI_Exception
    {
        int integer;

        double buff = Double.parseDouble(doubleValue);

        integer = (int)buff;

        if(buff - integer != 0)
            throw new VI_Exception(doubleValue + " : found DOUBLE, expected INTEGER");

        return integer;
    }

    /**
     *  Function params processing
     */
    private void split() throws VI_Exception
    {
        ArrayList<Object> result = new ArrayList<>();
        ArrayList<Object> buffer = new ArrayList<>();


        for(Object o : functionArguments)
        {
            if(o instanceof Token)
            {
                if(((Token) o).getTokenType() != TokenType.COMMA)
                {
                    buffer.add(o);
                }
                else
                {
                    if(buffer.size() > 1)
                    {
                        ArrayList<Token> tokBuf = new ArrayList<>();

                        for(Object obj : buffer)
                            if(obj instanceof Token)
                                tokBuf.add((Token) obj);
                            else
                                throw new VI_Exception("error, type mismatch");

                        result.add(new BinaryCalculation(tokBuf).getResult());
                    }
                    else
                        result.add(buffer.get(0));

                    buffer = new ArrayList<>();
                }
            }
            else
            {
                buffer.add(o);
            }


        }
        if(buffer.size() > 1)
        {
            ArrayList<Token> tokBuf = new ArrayList<>();

            for(Object obj : buffer)
                if(obj instanceof Token)
                    tokBuf.add((Token) obj);
                else
                    throw new VI_Exception("error, type mismatch");

            result.add(new BinaryCalculation(tokBuf).getResult());
        }
        else
            result.add(buffer.get(0));

        functionArguments = result;
    }

}