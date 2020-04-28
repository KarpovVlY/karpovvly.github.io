package com.company;

import com.company.Interpreter.Interpreter;
import com.company.Lexer.Lexer;
import com.company.Lexer.Service.Token;
import com.company.Parser.Parser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Master
{

    Master() throws VI_Exception
    {
        ArrayList<ArrayList<Token>> tokensArray = new Lexer(readFile()).getTokensArray();


        //showTokens(tokensArray);

        Interpreter masterInterpreter = new Interpreter();


        int position = 0;
        for (; position < tokensArray.size(); position++)
        {
            ArrayList<Token> tokens = tokensArray.get(position);

            Parser parser = new Parser(tokens);

            if (!parser.blockFinished)
            {
                while(!parser.blockFinished)
                {
                    ++position;
                    if(position == tokensArray.size())
                        break;
                    tokens = tokensArray.get(position);
                    position = parser.fillBlock(tokens, position);
                }

                masterInterpreter.execute(parser.getTerm());
            }
            else
                masterInterpreter.execute(new Parser(tokens).getTerm());
        }
    }


    private void showTokens(ArrayList<ArrayList<Token>> tokenArray)
    {
        for(ArrayList<Token> tokenArrayList : tokenArray)
            for(Token token : tokenArrayList)
                System.out.println(token.getContent() + "  " + token.getTokenType());
    }

    private String readFile()
    {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileReader fr = new FileReader("D:\\Programms\\VI\\src\\com\\company\\Text");
            Scanner scan = new Scanner(fr);

            while (scan.hasNextLine())
                stringBuilder.append(scan.nextLine()).append("\n");

            fr.close();
        } catch (Exception ignored) { }

        return stringBuilder.toString();
    }


    private String getLine(String line)
    {


        return line;
    }

}