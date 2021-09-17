package Lexer;

import java.util.*;

enum TOKEN_TYPE {
    TOKEN_NULL, TOKEN_CHAR, TOKEN_DIGIT, TOKEN_STRING, TOKEN_SYMBOL, TOKEN_SPACE, TOKEN_SINGLE
}

public class Token {

    public TOKEN_TYPE id;
    public String token;
    public int line;

    public static HashMap<Character, TOKEN_TYPE> tokens;


    public Token(int line, String token, TOKEN_TYPE id) {
        this.line = line;
        this.token = token;
        this.id = id;
    }

    public Token() {
        this.line = 0;
        this.id = TOKEN_TYPE.TOKEN_NULL;
    }

    public static String typeToString(TOKEN_TYPE type) {
        switch(type) {
            case TOKEN_CHAR:
            return "Identifier";
            case TOKEN_DIGIT:
            return "Digit";
            case TOKEN_SYMBOL:
            return "Sybmol";
            case TOKEN_STRING:
            case TOKEN_SINGLE:
            return "String";
            case TOKEN_SPACE:
            return "Space";
            case TOKEN_NULL:
            return "NULL";
        }
        return "NULL_TYPE";
    }

    public static void build_map() {
        tokens = new HashMap<Character, TOKEN_TYPE>();
        for(char i = 0; i < 255; ++i) 
            tokens.put(i, TOKEN_TYPE.TOKEN_NULL);
 
        for(char i = 'a'; i <= 'z'; ++i)
            tokens.put(i, TOKEN_TYPE.TOKEN_CHAR);

        for(char i = 'A'; i <= 'Z'; ++i)
            tokens.put(i, TOKEN_TYPE.TOKEN_CHAR);


        for(char i = '0'; i <= '9'; ++i)
            tokens.put(i, TOKEN_TYPE.TOKEN_DIGIT);
        
        tokens.put('=', TOKEN_TYPE.TOKEN_SYMBOL);
        tokens.put('<', TOKEN_TYPE.TOKEN_SYMBOL);
        tokens.put('>', TOKEN_TYPE.TOKEN_SYMBOL);
        tokens.put('{', TOKEN_TYPE.TOKEN_SYMBOL);
        tokens.put('}', TOKEN_TYPE.TOKEN_SYMBOL);
        tokens.put('(', TOKEN_TYPE.TOKEN_SYMBOL);
        tokens.put(')', TOKEN_TYPE.TOKEN_SYMBOL);
        tokens.put('[', TOKEN_TYPE.TOKEN_SYMBOL);
        tokens.put(']', TOKEN_TYPE.TOKEN_SYMBOL);
        tokens.put(';', TOKEN_TYPE.TOKEN_SYMBOL);
        tokens.put(':', TOKEN_TYPE.TOKEN_SYMBOL);
        tokens.put('!', TOKEN_TYPE.TOKEN_SYMBOL);
        tokens.put('+', TOKEN_TYPE.TOKEN_SYMBOL);
        tokens.put('-', TOKEN_TYPE.TOKEN_SYMBOL);
        tokens.put('*', TOKEN_TYPE.TOKEN_SYMBOL);
        tokens.put('/', TOKEN_TYPE.TOKEN_SYMBOL);
        tokens.put('-', TOKEN_TYPE.TOKEN_SYMBOL);
        tokens.put(',', TOKEN_TYPE.TOKEN_SYMBOL);
        tokens.put('.', TOKEN_TYPE.TOKEN_SYMBOL);
        tokens.put(' ', TOKEN_TYPE.TOKEN_SPACE);
        tokens.put('\t', TOKEN_TYPE.TOKEN_SPACE);
        tokens.put('\r', TOKEN_TYPE.TOKEN_SPACE);
        tokens.put('\n', TOKEN_TYPE.TOKEN_SPACE);
        tokens.put('\"', TOKEN_TYPE.TOKEN_STRING);
        tokens.put('\'', TOKEN_TYPE.TOKEN_SINGLE);
    }

    public String toString() {
        return "[" + line + ", " + token + ", " + typeToString(id) + "]";
    }

}