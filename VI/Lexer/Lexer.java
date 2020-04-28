package com.company.Lexer;




import com.company.Lexer.Service.Token;
import com.company.Lexer.Service.TokenType;
import com.company.VI_Exception;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer
{

    public ArrayList<ArrayList<Token>> getTokensArray() { return tokensArray; }

    private final ArrayList<ArrayList<Token>> tokensArray = new ArrayList<>();

    public Lexer(String input) throws VI_Exception
    {
        StringTokenizer stringTokenizer = new StringTokenizer(input, "\t\r\n");

        while(stringTokenizer.hasMoreTokens())
            tokensArray.add(processAddictiveTokens(stringTokenizer.nextToken()));

    }

    private ArrayList<Token> processAddictiveTokens(String currentTokenContent) throws VI_Exception {
        ArrayList<Token> tokens = new ArrayList<>();


        Pattern pattern = Pattern.compile
                ("(\\b(?:function|var|if|else|while|for|do|true|false|print|println|Hash|List|Array" +
                        "|setSize|getSize|length|replace|clear" +
                        "|get|getFirst|getLast|add|addForward|addBackward|remove|removeFirst|removeLast" +
                        "|set|put|peek|pop|isEmpty|containsKey|containsValue)\\b)" +
                        "|([A-Za-z][A-Za-z0-9]*)|(\\d?\\d++\\.\\d++)|(\\d?\\d++)|([\\=\\>\\<\\!]?\\=)" +
                        "|(\\&\\&|\\|\\|)|([\\+\\-\\*\\/\\%]|[\\(\\)\\{\\}]|[\\~\\:\\!\\,]|[\\>\\<])" +
                        "|(.)");

        Matcher matcher = pattern.matcher(currentTokenContent);


            while (matcher.find())
            {

                if(matcher.group(1) != null)
                    tokens.add(new Token(matcher.group(1), checkCommands(matcher.group(1))));
                else if(matcher.group(2) != null)
                    tokens.add(new Token(matcher.group(2), TokenType.ID));
                else if(matcher.group(3) != null)
                    tokens.add(new Token(matcher.group(3), TokenType.NUMBER));
                else if(matcher.group(4) != null)
                    tokens.add(new Token(matcher.group(4), TokenType.NUMBER));
                else if(matcher.group(5) != null)
                    tokens.add(new Token(matcher.group(5), getOperandType(matcher.group(5))));
                else if(matcher.group(6) != null)
                    tokens.add(new Token(matcher.group(6), getOperandType(matcher.group(6))));
                else if(matcher.group(7) != null)
                    tokens.add(new Token(matcher.group(7), getOperandType(matcher.group(7))));
                else if(matcher.group(8) != null)
                {
                    String a = matcher.group(8);


                    if (matcher.group(8).compareTo(" ") != 0 && matcher.group(8).compareTo("") != 0)
                        throw new VI_Exception(matcher.group(8) + "unknown symbol");
                }
            }

            return tokens;


        /*while (matcher.find())
        {
            if(matcher.group(1) != null)
                tokens.add(new Token(matcher.group(1), checkCommands(matcher.group(1))));
            else if(matcher.group(2) != null)
                tokens.add(new Token(matcher.group(2), TokenType.ID));
            else if(matcher.group(3) != null)
                tokens.add(new Token(matcher.group(3), TokenType.NUMBER));
            else if(matcher.group(4) != null)
                tokens.add(new Token(matcher.group(4), TokenType.NUMBER));
            else if(matcher.group(5) != null)
                tokens.add(new Token(matcher.group(5), getOperandType(matcher.group(5))));
            else if(matcher.group(6) != null)
                tokens.add(new Token(matcher.group(6), getOperandType(matcher.group(6))));
            else if(matcher.group(7) != null)
                tokens.add(new Token(matcher.group(7), getOperandType(matcher.group(7))));


        }*/


    }


    private TokenType getOperandType(String operand)
    {
        if(operand.compareTo("+") == 0)
            return TokenType.PLUS;
        else if(operand.compareTo("-") == 0)
            return TokenType.MINUS;
        else if(operand.compareTo("*") == 0)
            return TokenType.MULTIPLY;
        else if(operand.compareTo("/") == 0)
            return TokenType.DIVIDE;
        else if(operand.compareTo("%") == 0)
            return TokenType.PERCENT;
        else if(operand.compareTo("=") == 0)
            return TokenType.EQUAL_MARK;
        else if(operand.compareTo(",") == 0)
            return TokenType.COMMA;
        else if(operand.compareTo("\"") == 0)
            return TokenType.QUOTE;
        else if(operand.compareTo(":") == 0)
            return TokenType.COLON;
        else if(operand.compareTo("~") == 0)
            return TokenType.TILDE;
        else if(operand.compareTo("!") == 0)
            return TokenType.EXCLAMATION_MARK;
        else if(operand.compareTo("?") == 0)
            return TokenType.QUESTION_MARK;
        else if(operand.compareTo("(") == 0)
            return TokenType.LEFT_ROUNDED_BRACKET;
        else if(operand.compareTo(")") == 0)
            return TokenType.RIGHT_ROUNDED_BRACKET;
        else if(operand.compareTo("{") == 0)
            return TokenType.LEFT_BRACKET;
        else if(operand.compareTo("}") == 0)
            return TokenType.RIGHT_BRACKET;
        else if(operand.compareTo("[") == 0)
            return TokenType.LEFT_SQUARE_BRACKET;
        else if(operand.compareTo("]") == 0)
            return TokenType.RIGHT_SQUARE_BRACKET;
        else if(operand.compareTo("!=") == 0)
            return TokenType.NEGATIVE_EQUAL;
        else if(operand.compareTo("==") == 0)
            return TokenType.POSITIVE_EQUAL;
        else if(operand.compareTo("<") == 0)
            return TokenType.LESS;
        else if(operand.compareTo("<=") == 0)
            return TokenType.LESS_EQUAL;
        else if(operand.compareTo(">") == 0)
            return TokenType.MORE;
        else if(operand.compareTo(">=") == 0)
            return TokenType.MORE_EQUAL;
        else if(operand.compareTo("&&") == 0)
            return TokenType.AND;
        else if(operand.compareTo("||") == 0)
            return TokenType.OR;
        else
            return null;
    }

    private TokenType checkCommands(String currentCommand)
    {
        if(currentCommand.compareTo("var") == 0)
            return TokenType.VARIABLE_DECLARATION;
        else if(currentCommand.compareTo("true") == 0 || currentCommand.compareTo("false") == 0)
            return TokenType.BOOLEAN;
        else if(currentCommand.compareTo("function") == 0)
            return TokenType.FUNCTION_DECLARATION;
        else if(currentCommand.compareTo("if") == 0)
            return TokenType.IF_STATEMENT;
        else if(currentCommand.compareTo("else") == 0)
            return TokenType.ELSE_STATEMENT;
        else if(currentCommand.compareTo("while") == 0)
            return TokenType.WHILE_STATEMENT;
        else if(currentCommand.compareTo("for") == 0)
            return TokenType.FOR_STATEMENT;
        else if(currentCommand.compareTo("print") == 0)
            return TokenType.PRINT;
        else if(currentCommand.compareTo("println") == 0)
            return TokenType.PRINTLN;
        else if(currentCommand.compareTo("Array") == 0)
            return TokenType.ARRAY;
        else if(currentCommand.compareTo("List") == 0)
            return TokenType.LIST;
        else if(currentCommand.compareTo("Hash") == 0)
            return TokenType.HASH;
        else
            return TokenType.FUNCTION;

    }
}
