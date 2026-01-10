package lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {

    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    private int start = 0;
    private int current = 0;
    private int line = 1;

    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("if", TokenType.IF);
        keywords.put("print", TokenType.PRINT);
        keywords.put("else", TokenType.ELSE);
        keywords.put("while", TokenType.WHILE);
        keywords.put("true", TokenType.TRUE);
        keywords.put("false", TokenType.FALSE);
        keywords.put("exit", TokenType.EXIT);
        keywords.put("for", TokenType.FOR);
        keywords.put("to", TokenType.TO);
        keywords.put("fun", TokenType.FUN);
        keywords.put("return", TokenType.RETURN);
        keywords.put("and", TokenType.AND);
        keywords.put("or", TokenType.OR);
        keywords.put("not", TokenType.NOT);
        keywords.put("break", TokenType.BREAK);
        keywords.put("continue", TokenType.CONTINUE);
        keywords.put("class", TokenType.CLASS);
        keywords.put("this", TokenType.THIS);
        keywords.put("null", TokenType.NULL);

    }

    public Lexer(String source) {
        this.source = source;
    }

    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    private void scanToken() {
    char c = advance();

    switch (c) {
        case '(':
            addToken(TokenType.LEFT_PAREN);
            break;
        case ')':
            addToken(TokenType.RIGHT_PAREN);
            break;
        case '{':
            addToken(TokenType.LEFT_BRACE);
            break;
        case '}':
            addToken(TokenType.RIGHT_BRACE);
            break;
        case ':':
            addToken(TokenType.COLON);
            break;
        case '[':
    addToken(TokenType.LEFT_BRACKET);
    break;
case ']':
    addToken(TokenType.RIGHT_BRACKET);
    break;
        case '+':
            addToken(TokenType.PLUS);
            break;
        case '-':
            addToken(TokenType.MINUS);
            break;
        case '*':
            addToken(TokenType.STAR);
            break;
        case '/':
            addToken(TokenType.SLASH);
            break;
        case '%':
            addToken(TokenType.PERCENT);
            break;
        case '.':
            addToken(TokenType.DOT);
            break;

        case '=':
    addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
    break;
case '>':
    addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
    break;
case '<':
    addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
    break;
case '!':
    if (match('=')) {
        addToken(TokenType.BANG_EQUAL);
    } else {
        System.err.println("Unexpected '!' at line " + line);
    }
    break;


        case ' ':
        case '\r':
        case '\t':
            break;
        case '#':
    // Comment: skip until end of line
    while (peek() != '\n' && !isAtEnd()) advance();
    break;

        case '\n':
            line++;
            break;

        case '"':
            string();
            break;

        case ',':
            addToken(TokenType.COMMA);
            break;

        default:
            if (isDigit(c)) {
                number();
            } else if (isAlpha(c)) {
                identifier();
            } else {
                System.err.println("Unexpected character at line " + line + ": " + c);
            }
            break;
    }
}


    private void identifier() {
        while (isAlphaNumeric(peek())) advance();

        String text = source.substring(start, current);
        TokenType type = keywords.getOrDefault(text, TokenType.IDENTIFIER);
        addToken(type);
    }

    private void number() {
        while (isDigit(peek())) advance();

        if (peek() == '.' && isDigit(peekNext())) {
            advance();
            while (isDigit(peek())) advance();
        }

        double value = Double.parseDouble(source.substring(start, current));
        addToken(TokenType.NUMBER, value);
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }

        if (isAtEnd()) {
            System.err.println("Unterminated string at line " + line);
            return;
        }

        advance(); // closing quote
        String value = source.substring(start + 1, current - 1);
        addToken(TokenType.STRING, value);
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;
        current++;
        return true;
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') ||
               c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private char advance() {
        return source.charAt(current++);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }
}
