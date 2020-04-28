package com.company.Interpreter.Value.HashTableValue;


import com.company.Interpreter.Value.VariableValue;
import com.company.Lexer.Service.Token;
import com.company.Lexer.Service.TokenType;
import com.company.VI_Exception;

import java.util.ArrayList;
import java.util.Random;

public class VI_HashTable<Key, Value> extends VariableValue
{

    private ArrayList<VI_HashTableNode<Key, Value>> hashTable;

    public VI_HashTable()
    {
        hashTable = new ArrayList<>();
    }

    @Override
    public TokenType getValueType() { return TokenType.HASH; }

    public void put(Key key, Value value) throws VI_Exception
    {
        VI_HashTableNode<Key, Value> node = getNodeByKey(key);

        if (node != null)
        {
            node.setValue(value);
        }
        else
        {
            hashTable.add(null);

            int position = getPositionByKey(key);

            if (hashTable.get(position) != null)
            {
                position = findExtendedPosition(position);
                if(position == -1)
                    throw new VI_Exception("Unexpected error");

            }

            hashTable.set(position, new VI_HashTableNode<>(key, value));
        }
    }


    public void replace(Key key, Value value)
    {
        VI_HashTableNode<Key, Value> node = getNodeByKey(key);

        if(node != null)
            node.setValue(value);
    }


    public Value get(Key key)
    {
        if (key == null)
            return null;

        VI_HashTableNode<Key, Value> node = getNodeByKey(key);

        return node == null ? null : node.getValue();
    }


    public boolean containsKey(Key key)
    {
        int position = getPositionByKey(key);

        for(int i = position ; i < hashTable.size() ; i ++)
        {
            VI_HashTableNode<Key, Value> node = hashTable.get(i);

            if(((String)node.getKey()).compareTo(key.toString()) == 0)
                return true;
        }

        return false;
    }

    public boolean containsValue(Value value)
    {
        for(VI_HashTableNode<Key, Value> node : hashTable)
            if(equal(node.getValue(), value))
                return true;

        return false;
    }

    private boolean equal(Value left, Value right)
    {
        return ((Token) left).getContent().compareTo(((Token) right).getContent()) == 0
                && ((Token) left).getTokenType() == ((Token) right).getTokenType();
    }

    public void remove(Key key)
    {
        int position = getPositionByKey(key);

        for(int i = position ; i < hashTable.size() ; i ++)
        {
            VI_HashTableNode<Key, Value> node =  hashTable.get(i);

            if(node != null && ((String)node.getKey()).compareTo(key.toString()) == 0)
            {
                hashTable.remove(i);
                return;
            }

        }
    }

    public void clear()
    {
        hashTable = new ArrayList<>();
    }

    public int getSize() { return hashTable.size(); }
    public boolean isEmpty() { return hashTable.isEmpty(); }



    private int findExtendedPosition(int position)
    {
        for(int i = position; i < hashTable.size(); i++)
            if(hashTable.get(i)  == null)
                return i;

       return -1;
    }


    private VI_HashTableNode<Key, Value> getNodeByKey(Key key)
    {
        int position = getPositionByKey(key);

        for(int i = position ; i < hashTable.size() ; i ++)
        {
            VI_HashTableNode<Key, Value> node = hashTable.get(i);

            if(node != null && ((String)node.getKey()).compareTo(key.toString()) == 0)
                return node;
        }


        return null;
    }



    private int getPositionByKey(Key key)
    {
        double buffer = key.hashCode() * new Random().nextInt(1);
        buffer -= (int) buffer;

        return (int) (buffer * hashTable.size());
    }
}

class VI_HashTableNode<Key, Value>
{
    private Key key;
    private Value value;

    VI_HashTableNode(Key k, Value v)
    {
        key = k;
        value = v;
    }


    public Key getKey() { return key; }

    public Value getValue() { return value; }
    public void setValue(Value value) { this.value = value; }

}