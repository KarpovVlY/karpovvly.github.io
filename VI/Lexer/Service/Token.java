package com.company.Lexer.Service;

public class Token
{
    private TokenType tokenType;
    private String content;


    public Token(String content, TokenType tokenType)
    {
        this.content = content;
        this.tokenType = tokenType;
    }

    public TokenType getTokenType() { return tokenType; }
    public String getContent() { return content; }
}
