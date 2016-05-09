package org.jdbc.dynsql.lexer.token.impl;

import java.util.ArrayList;
import java.util.List;

import org.jdbc.dynsql.lexer.token.LexerToken;

public class LexerTokenTemplate implements LexerToken {

    private String expression;
    private List<LexerToken> tokens = new ArrayList<LexerToken>();
    private int lineNumber = 0;
    private int charNumber = 0;
    
    public void setExpression(String expression) {
       this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }

    public String[] getTokenComponents() {
        return null;
    }

    public List<LexerToken> getTokens() {
        // TODO Auto-generated method stub
        return tokens;
    }

    public void addToken(LexerToken token) {
        this.tokens.add(token);
    }

    public void setStartingOfTemplate(int startingOfLine, int startingOfChar) {
        lineNumber = startingOfLine;
        charNumber = startingOfChar;
    }
    
    @Override
    public String toString() {
        return String.format("TOKEN TEMPLATE");
    }

	public void setAssociatedObject(Object object) {
		// TODO Auto-generated method stub
		
	}

	public Object getAssociatedObject() {
		// TODO Auto-generated method stub
		return null;
	}
}
