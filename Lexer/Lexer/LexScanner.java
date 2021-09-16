package Lexer;

import java.util.*;
import java.io.*;

public class LexScanner {

    private String string_data;
    private int line, index;

    public LexScanner(String data) {
        Token.build_map();
        string_data = data;
        line = 1;
        index = 0;
    }

    private char curchar() {
        if(index < string_data.length())
            return string_data.charAt(index);
        return 0;
    }

    private char getchar() {
        if(index < string_data.length()) { 
            char ch = string_data.charAt(index);
            index++;
            if(ch == '\n')
            line++;
            return ch;
        }
        return 0;
    }

    private char peekchar() {
        if(index+1 < string_data.length()) {
            return string_data.charAt(index+1);
        }
        return 0;
    }

    private String grabChar() {
        String tok = new String();
        char ch = getchar();
        tok += ch;
        TOKEN_TYPE state;
        state = Token.tokens.get(ch);
        while(ch != 0 && state == TOKEN_TYPE.TOKEN_CHAR || state == TOKEN_TYPE.TOKEN_DIGIT) {
            ch = getchar();
            state = Token.tokens.get(ch);
            if(state == TOKEN_TYPE.TOKEN_CHAR || state == TOKEN_TYPE.TOKEN_DIGIT)
                tok += ch;
            else {
                if(peekchar() == 0)
                return tok;
                else {
                    index--;
                    return tok;
                }
            }
        }
        return tok;
    }

    private String grabDigits() {
        String tok = new String();
        TOKEN_TYPE state = TOKEN_TYPE.TOKEN_DIGIT;
        char ch = getchar();
        tok += ch; 
        while(state == TOKEN_TYPE.TOKEN_DIGIT || peekchar() == '.') {
            ch = getchar();
            state = Token.tokens.get(ch);
            if(state == TOKEN_TYPE.TOKEN_DIGIT || ch == '.') {
                tok += ch;
                state = TOKEN_TYPE.TOKEN_DIGIT;
            } else if(ch != 0) {
                index--;
                return tok;
            }
        }
        return tok;
    }

    private String grabSymbol() {
        String tok = new String();
        tok += getchar();
        char ch = curchar();
        if(ch == '=') {
            tok += ch;
            index++;
        }
        return tok;
    }

    private String grabString() {
        String tok = new String();
        char ch = getchar();
        TOKEN_TYPE state = Token.tokens.get(peekchar());

        while(ch != 0 && state != TOKEN_TYPE.TOKEN_STRING && ch != 0) {
            ch = getchar();
            if(ch == '\\') {
                if(getchar() == '"')
                    tok += '"';
                else if(curchar() == 'n') {
                    tok += "\n";
                    index--;
                }
                else if(curchar() == 't') {
                    tok += "\t";
                    index--;
                }
                else if(curchar() == 'r') {
                    tok += "\r";
                    index--;
                }
                continue;
            }
            if(ch != '"') {
                tok += ch;
            }
            state = Token.tokens.get(ch);
        }
        return tok;
    }

    public ArrayList<Token> Lex() {
        ArrayList<Token> list = new ArrayList<Token>();
        TOKEN_TYPE state;
        while(true) {
            char ch = curchar();
            if(ch == 0)
            return list;
            state = Token.tokens.get(ch);
            Token t = new Token();
            switch(state) {
                case TOKEN_NULL:
                    index++;
                continue;
                case TOKEN_DIGIT:
                    t = new Token(line, grabDigits(), TOKEN_TYPE.TOKEN_DIGIT);
                break;
                case TOKEN_CHAR:
                    t = new Token(line, grabChar(), TOKEN_TYPE.TOKEN_CHAR);
                break;
                case TOKEN_SYMBOL:
                    t = new Token(line, grabSymbol(), TOKEN_TYPE.TOKEN_SYMBOL);
                break;
                case TOKEN_STRING:
                    t = new Token(line, grabString(), TOKEN_TYPE.TOKEN_STRING);
                break;
                case TOKEN_SPACE:
                    if(ch == '\n') line++;
                         index++;
                continue;
            }
            list.add(t);
        }
    }

    public static void main(String args[]) {
        if(args.length == 0) {
            repl();
            System.exit(0);
        } else {
            lexFile(args[0]);
            System.exit(0);
        }
    }

    public static void repl() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            try {
                System.out.print("> ");
                String line = br.readLine();
                LexScanner scanner = new LexScanner(line);
                ArrayList<Token> list = scanner.Lex();
                for(Token t : list) {
                    if(t.token.equals("quit") == true) System.exit(0);
                    System.out.println("Token: " + t.token + " [" + Token.typeToString(t.id) + "]");
                }
            } catch (IOException ioe) {
               System.out.println(ioe);
            } 
        }
    }
    public static void lexFile(String in_file) {
        try {
            File file = new File(in_file);
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            String str = new String(data, "UTF-8");
            LexScanner scanner = new LexScanner(str);
            ArrayList<Token> list = scanner.Lex();
            OutputHTML(list);
        } catch(IOException io) {
            System.out.println(io);
        }
    }

    public static void OutputHTML(ArrayList<Token> list) { 
        System.out.print("<!doctype html>\n<html><head><title>Lexer Output</title></head><body>\n");
        System.out.print("<table border=\"1\" cellpadding=\"5\" cellspacing=\"3\"><tr><th>Line</th><th>Token</th><th>Type</th></tr>\n");
        for(Token i : list) {
            System.out.print("<tr><th>" + i.line + "</th><th>" + i.token + "</th><th>" + Token.typeToString(i.id) + "</th></tr>\n");
        }
        System.out.print("</table></body></html>\n");
    }
}