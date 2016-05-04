package org.jdbc.dynsql.lexer;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private String[] template;

    private int line;
    private int index;

    public Lexer() {
        this.line = 0;
        this.index = 0;
    }

    public void setTemplate(String[] template) {
        this.line = 0;
        this.index = 0;
        this.template = template;
    }

    public void setTemplate(String template) {
        this.line = 0;
        this.index = 0;
        this.template = template.split("\n");
    }

    public LexerToken nextToken() {
        LexerToken token = null;
        int startSearch = index;

        String stringLine = template[line].trim();

        // check command
        if (isCommand(stringLine)) {
            token = new LexerTokenCommand();
            token.setStartingOfTemplate(line, index);
            token.setExpression(stringLine);
            line++;
            return token;
        } else if (isSection(stringLine))
        {
            token = new LexerTokenSection();
            token.setStartingOfTemplate(line, index);
            token.setExpression(stringLine);
            line++;
            return token;
        }

        // check expression or content
        if (token == null) {
            boolean expressionFound = false;
            while (index < stringLine.length()) {
                if (stringLine.charAt(index) == '$' && stringLine.charAt(index + 1) == '{') {
                    // check found content
                    if (startSearch < index) {
                        token = new LexerTokenContent();
                        token.setStartingOfTemplate(line, index);
                        token.setExpression(stringLine.substring(startSearch, index));
                        return token;
                    } else {
                        expressionFound = true;
                    }
                } else if (expressionFound && stringLine.charAt(index) == '}') {
                    token = new LexerTokenExpression();
                    token.setStartingOfTemplate(line, index);
                    token.setExpression(stringLine.substring(startSearch, index + 1));
                    index++;
                    return token;
                }
                index++;
            }
        }

        // recognize end line
        if (startSearch < index) {
            token = new LexerTokenContent();
            token.setStartingOfTemplate(line, index);
            token.setExpression(stringLine.substring(startSearch, index));
            index++;
            return token;
        } else if (index >= stringLine.length()) {
            line++;
            index = 0;
            token = new LexerTokenCRLF();
            token.setStartingOfTemplate(line, index);
            return token;
        }
        return token;
    }

    public boolean hasMoreTokens() {
        if (line < template.length && index <= template[line].length() + 1)
            return true;
        return false;
    }

    public boolean isCommand(String line) {
        if (index == 0)
            for (LexerCommand command : LexerCommand.values()) {
                if (line.toLowerCase().startsWith("--" + command.toString().toLowerCase())) {
                    return true;
                }
            }
        return false;
    }
    
    public boolean isSection(String line)
    {
        return line.trim().startsWith("--##");
    }
}
