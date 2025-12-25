package lexer;

public enum TokenType {
    // Single-character tokens
    LEFT_PAREN, RIGHT_PAREN,
    LEFT_BRACE, RIGHT_BRACE,
    LEFT_BRACKET,RIGHT_BRACKET,


    // Operators
    PLUS, MINUS, STAR, SLASH,
    EQUAL, EQUAL_EQUAL,
    GREATER, LESS, TRUE, FALSE,
    LESS_EQUAL,
    GREATER_EQUAL,
    BANG_EQUAL,


    // Literals
    IDENTIFIER, NUMBER, STRING,

    // Keywords
    IF, PRINT, ELSE, WHILE,FOR, TO,


    // Special
    EOF, COMMA, EXIT,
}
