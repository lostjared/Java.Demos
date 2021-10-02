package com.lostsidedead.sourcelexer;
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

    private void skipComment() {
        char ch = getchar();
        while(ch != '\n' && ch != 0) {
            ch = getchar();
        }
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
            else if(state == TOKEN_TYPE.TOKEN_NULL)
                return tok;
            else {
                index--;
                return tok;
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
        char fch = getchar();
        tok += fch;
        char ch = curchar();
        if(ch == '=') {
            tok += ch;
            index++;
        } else if(fch == '=' || fch == '&' || fch == '|' || fch == '+' || fch == '-' || fch == '>' || fch == '<' || fch == '*') {
            if(fch == ch) {
                tok += ch;
                index++;
            }
        }
        return tok;
    }

    private String grabString() {
        String tok = new String();
        char ch = getchar();
        TOKEN_TYPE state = Token.tokens.get(peekchar());
        if(state == TOKEN_TYPE.TOKEN_STRING) {
            tok += getchar();
            ++index;
            return tok;
        }
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

    private String grabSingle() {
        String tok = new String();
        char ch = getchar();
        TOKEN_TYPE state = Token.tokens.get(peekchar());
        if(state == TOKEN_TYPE.TOKEN_SINGLE) {
            tok += getchar();
            ++index;
            return tok;
        }
        while(ch != 0 && state != TOKEN_TYPE.TOKEN_SINGLE && ch != 0) {
            ch = getchar();
            if(ch == '\\') {
                if(getchar() == '\'')
                    tok += '\"';
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
            if(ch != '\'') {
                tok += ch;
            }
            state = Token.tokens.get(ch);
        }
        return tok;
    }

    public void reset() {
        index = 0;
        line = 1;
    }

    public ArrayList<Token> Lex() {
        ArrayList<Token> list = new ArrayList<Token>();
        TOKEN_TYPE state;
        while(true) {
            char ch = curchar();
            if(ch == 0)
                return list;
            else if(ch == '/' && peekchar() == '/') {
                ++index;
                skipComment();
                continue;
            }
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
                case TOKEN_SINGLE:
                    t = new Token(line, grabSingle(), TOKEN_TYPE.TOKEN_SINGLE);
                    break;
                case TOKEN_SPACE:
                    if(ch == '\n') line++;
                    index++;
                    continue;
            }
            list.add(t);
        }
    }

    public boolean isAnotherToken() {
        if(index < string_data.length())
            return true;
        return false;
    }

    public Token nextToken() {
        TOKEN_TYPE state;
        Token t = new Token();
        while(true) {
            char ch = curchar();
            if(ch == 0)
                return null;
            else if(ch == '/' && peekchar() == '/') {
                ++index;
                skipComment();
                continue;
            }
            state = Token.tokens.get(ch);
            switch(state) {
                case TOKEN_NULL:
                    index++;
                    continue;
                case TOKEN_DIGIT:
                    return new Token(line, grabDigits(), TOKEN_TYPE.TOKEN_DIGIT);
                case TOKEN_CHAR:
                    return new Token(line, grabChar(), TOKEN_TYPE.TOKEN_CHAR);
                case TOKEN_SYMBOL:
                    return new Token(line, grabSymbol(), TOKEN_TYPE.TOKEN_SYMBOL);
                case TOKEN_STRING:
                    return new Token(line, grabString(), TOKEN_TYPE.TOKEN_STRING);
                case TOKEN_SINGLE:
                    return new Token(line, grabSingle(), TOKEN_TYPE.TOKEN_SINGLE);
                case TOKEN_SPACE:
                    if(ch == '\n') line++;
                    index++;
                    continue;
            }

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

    public static String lexFileString(String in_file) {
        String str = new String();
        try {
            File file = new File(in_file);
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            str = new String(data, "UTF-8");
            str = lexString(str);
        } catch(IOException ioe) {
            System.out.println(ioe);
        }
        return str;
    }

    public static void lexFile(String in_file, String out_file) {
        try {
            File file = new File(in_file);
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            String str = new String(data, "UTF-8");
            LexScanner scanner = new LexScanner(str);
            ArrayList<Token> list = new ArrayList<Token>();
            while(scanner.isAnotherToken()) {
                Token t = scanner.nextToken();
                if(t != null)
                    list.add(t);
            }
            if(out_file == null)
                OutputHTML(list);
            else
                OutputHTML(out_file, list);

        } catch(IOException io) {
            System.out.println(io);
        }
    }

    public static String lexString(String in_file) {
        String d = new String();
        d += "<!doctype html>\n<html><head><title>Lexer Output</title></head><body>\n";
        d += "<table border=\"1\" cellpadding=\"5\" cellspacing=\"3\"><tr><th>Line</th><th>Token</th><th>Type</th></tr>\n";
        LexScanner scanner = new LexScanner(in_file);
        while (scanner.isAnotherToken()) {
            Token t = scanner.nextToken();
            if (t != null) {
                d += OutputHTML_Token(t);
            }
        }
        d += "</table><br><br><br><br><br><br></body></html>\n";
        return d;
    }

    public static String OutputHTML_Token(Token i) {
        return "<tr><th>" + i.line + "</th><th>" + i.convertIntoHTML() + "</th><th>" + Token.typeToString(i.id) + "</th></tr>\n";
    }

    public static void OutputHTML(ArrayList<Token> list) {
        System.out.print("<!doctype html>\n<html><head><title>Lexer Output</title></head><body>\n");
        System.out.print("<table border=\"1\" cellpadding=\"5\" cellspacing=\"3\"><tr><th>Line</th><th>Token</th><th>Type</th></tr>\n");
        for(Token i : list) {
            System.out.print("<tr><th>" + i.line + "</th><th>" + i.convertIntoHTML() + "</th><th>" + Token.typeToString(i.id) + "</th></tr>\n");
        }
        System.out.print("</table></body></html>\n");
    }

    public static String OutputHTML_String(ArrayList<Token> list) {
        String d = new String();
        d += "<!doctype html>\n<html><head><title>Lexer Output</title></head><body>\n";
        d += "<table border=\"1\" cellpadding=\"5\" cellspacing=\"3\"><tr><th>Line</th><th>Token</th><th>Type</th></tr>\n";
        for(Token i : list) {
            d += "<tr><th>" + i.line + "</th><th>" + i.convertIntoHTML() + "</th><th>" + Token.typeToString(i.id) + "</th></tr>\n";
        }
        d += "</table></body></html>\n";
        return d;
    }

    public static void OutputHTML(String filename, ArrayList<Token> list) {
        try {
            FileWriter fw = new FileWriter(filename);
            fw.write("<!doctype html>\n<html><head><title>Lexer Output</title></head><body>\n");
            fw.write("<table border=\"1\" cellpadding=\"5\" cellspacing=\"3\"><tr><th>Line</th><th>Token</th><th>Type</th></tr>\n");
            for(Token i : list) {
                fw.write("<tr><th>" + i.line + "</th><th>" + i.convertIntoHTML() + "</th><th>" + Token.typeToString(i.id) + "</th></tr>\n");
            }
            fw.write("</table></body></html>\n");
            fw.close();
            System.out.println("Wrote to: " + filename);

        } catch(IOException ioe) {
            System.out.println(ioe);
        }
    }
}
