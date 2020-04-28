package com.company;


public class Main
{
    public static void main(String[] args)
    {
        try { new Master(); }
        catch(Exception e) { System.err.println(e.getMessage()); }
    }

}
