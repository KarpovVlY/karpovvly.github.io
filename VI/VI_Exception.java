package com.company;

import com.company.Lexer.Service.Token;

public class VI_Exception extends Exception
{
    public VI_Exception(String error) { super(error); }

    public VI_Exception(Token errorToken)
    { super(errorToken.getContent() + " : error in line");}

    public VI_Exception(Token firstErrorToken, Token secondErrorToken)
    { super(firstErrorToken.getContent() + " " + secondErrorToken.getContent() + " : error in line");}
}
