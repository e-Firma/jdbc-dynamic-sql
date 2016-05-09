package org.jdbc.dynsql.lexer.token.impl;

import java.util.List;

import org.jdbc.dynsql.exception.TemplateException;
import org.jdbc.dynsql.lexer.token.LexerToken;


public class LexerTokenCRLF implements LexerToken {

    private String expression = "\n";
    private int lineNumber = 0;
    private int charNumber = 0;

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }

    public String[] getTokenComponents() {
        return new String[] {expression};
    }
    
    public List<LexerToken> getTokens() {
        return null;
    }

    public void addToken(LexerToken token) {
    }
    
    public void setStartingOfTemplate(int startingOfLine, int startingOfChar) {
        lineNumber = startingOfLine;
        charNumber = startingOfChar;
    }
    
    @Override
    public String toString() {
        return String.format("TOKEN CLRF <%s,%s>", lineNumber, charNumber);
    }

	public void setAssociatedObject(Object object) {
		// TODO Auto-generated method stub
		
	}

	public Object getAssociatedObject() {
		// TODO Auto-generated method stub
		return null;
	}
}
