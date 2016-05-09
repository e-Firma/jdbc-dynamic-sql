package org.jdbc.dynsql.lexer.token.impl;

import java.util.List;

import org.jdbc.dynsql.lexer.token.LexerToken;

public class LexerTokenConverter {
    private int indent;
    private StringBuilder text;

    public String toString(LexerToken token) {
        indent = 0;
        text = new StringBuilder();
        text.append(indent());
        text.append(token);
        text.append("\n");
        if (token.getTokens() != null)
            toString(token.getTokens());
        return text.toString();
    }

    private void toString(List<LexerToken> tokens) {
        for (LexerToken token : tokens) {
            text.append(indent());
            text.append(token);
            text.append("\n");
            if (token.getTokens() != null) {
                indent++;
                toString(token.getTokens());
                indent--;
            }
        }
    }
    
    private String indent()
    {
        StringBuilder spaces = new StringBuilder();
        for(int pos = 0; pos < indent * 3; pos++)
        {
            spaces.append(" ");
        }
        return spaces.toString();
    }
}
