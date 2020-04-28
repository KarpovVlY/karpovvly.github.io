package com.company.Interpreter.Value.DoubleConnectedListValue;

import com.company.Interpreter.Value.VariableValue;
import com.company.Lexer.Service.Token;
import com.company.Lexer.Service.TokenType;
import com.company.VI_Exception;

import java.util.ListIterator;


public class VI_DoubleConnectedList<Item> extends VariableValue implements Iterable<Item>
{
    private VI_DoubleConnectedListItem<Item> head, tail;


    private int size;

    public VI_DoubleConnectedList() { size = 0; }

    @Override
    public TokenType getValueType() { return TokenType.LIST; }

    public void addForward(Item value)
    {
        VI_DoubleConnectedListItem<Item> item = new VI_DoubleConnectedListItem<>(value, null, null);

        if (head == null)
            tail = head = item;
        else
        {
            head.setPrevious(item);
            item.setNext(head);
            head = item;
        }

        ++size;
    }

    public void addBackward(Item value)
    {
        VI_DoubleConnectedListItem<Item> item = new VI_DoubleConnectedListItem<>(value, null, null);

        if (head == null)
            tail = head = item;
        else
        {
            tail.setNext(item);
            item.setPrevious(tail);
            tail = item;
        }

        ++size;
    }

    public void add(Item value)
    {
        addBackward(value);
    }

    public void add(Item value, int position) throws VI_Exception
    {
        if(position == 0)
        {
            addForward(value);
        }
        else if(position >= size)
        {
            addBackward(value);
        }
        else
        {
            VI_DoubleConnectedListItem<Item> buf = head;

            for(int i = 0 ; i < position - 1 ; i ++)
                buf = buf.getNext();


            VI_DoubleConnectedListItem<Item> item = new VI_DoubleConnectedListItem<>(value, buf.getNext(), buf);

            buf.getNext().setPrevious(item);
            buf.setNext(item);

            ++size;
        }
    }



    public Item getFirst() { return head.getValue(); }
    public Item getLast() { return tail.getValue(); }

    public Item get(int position) throws VI_Exception
    {
        if(!checkPosition(position))
            throw new VI_Exception("Index " + position + " is out of List");

        VI_DoubleConnectedListItem<Item> buf = head;

        for(int i = 0 ; i < position ; i ++)
            buf = buf.getNext();

        return buf.getValue();
    }

    public boolean isEmpty() { return size == 0; }

    public void set(Item value, int position) throws VI_Exception
    {
        if(!checkPosition(position))
            throw new VI_Exception("Index " + position + " is out of List");

        VI_DoubleConnectedListItem<Item> buf = head;

        for(int i = 0 ; i < position ; i ++)
            buf = buf.getNext();

        buf.setValue(value);
    }

    public void removeFirst()
    {
        head.getNext().setPrevious(null);
        head = head.getNext();
        size--;
    }

    public void removeLast()
    {
        tail.getPrevious().setNext(null);
        tail = tail.getPrevious();
        size--;
    }

    public void remove(int position) throws VI_Exception
    {
        if(!checkPosition(position))
            throw new VI_Exception("Index " + position + " is out of List");

        VI_DoubleConnectedListItem<Item> buf = head;

        for(int i = 0 ; i < position ; i ++)
            buf = buf.getNext();


        if(buf.getNext() == null)
        {
            buf.getPrevious().setNext(null);
            tail = buf.getPrevious();
        }
        else if(buf.getPrevious() == null)
        {
            buf.getNext().setPrevious(null);
            head = buf.getNext();
        }
        else
        {
            buf.getPrevious().setNext(buf.getNext());
            buf.getNext().setPrevious(buf.getPrevious());
        }



        size--;
    }


    public Item pop()
    {
        Item result= getLast();
        removeLast();
        return result;
    }


    public Item peek()
    {
        return getLast();
    }



    public void clear()
    {
        head = null;
        tail = null;
        size = 0;
    }

    public int getSize() { return size; }

    private boolean checkPosition(int position) { return position < size + 1; }



    VI_DoubleConnectedListItem<Item> getHead() { return head; }
    VI_DoubleConnectedListItem<Item> getTail() { return tail; }


    public ListIterator<Item> iterator() { return new VI_DoubleConnectedListIterator<>(this); }


}


class VI_DoubleConnectedListIterator<Item> implements ListIterator<Item>
{
    private VI_DoubleConnectedListItem<Item> current;


    VI_DoubleConnectedListIterator(VI_DoubleConnectedList<Item> list)
    {
        current = list.getHead();
    }

    @Override
    public boolean hasNext() { return current != null; }

    @Override
    public Item next()
    {
        System.out.println(current);
        Item data = current.getValue();
        current = current.getNext();
        return data;
    }

    @Override
    public boolean hasPrevious() { return current != null; }

    @Override
    public Item previous()
    {
        Item data = current.getValue();
        current = current.getPrevious();
        return data;
    }

    @Override
    public void remove()
    {
        throw new UnsupportedOperationException();
    }


    @Override
    public int nextIndex() { return 0; }

    @Override
    public int previousIndex() { return 0; }

    @Override
    public void set(Item value) { }

    @Override
    public void add(Item value) { }
}


class VI_DoubleConnectedListItem<Item>
{
    private Item value;

    private VI_DoubleConnectedListItem<Item> previous;
    private VI_DoubleConnectedListItem<Item> next;

    VI_DoubleConnectedListItem(Item data, VI_DoubleConnectedListItem<Item> next, VI_DoubleConnectedListItem<Item> previous)
    {
        this.value = data;
        this.previous = previous;
        this.next = next;
    }



    void setNext(VI_DoubleConnectedListItem<Item> next) { this.next = next; }
    void setPrevious(VI_DoubleConnectedListItem<Item> previous) { this.previous = previous; }


    void setValue(Item value) { this.value = value; }
    Item getValue() { return value; }

    VI_DoubleConnectedListItem<Item> getNext() { return next; }
    VI_DoubleConnectedListItem<Item> getPrevious() { return previous; }
}