package org.jdbc.dynsql.lexer.token;

import java.util.List;

public interface LexerToken {
    void setExpression(String expression);
    String getExpression();
    String[] getTokenComponents();
    void setTemplateBeginning(int lineNumber, int charNumber);
    void addToken(LexerToken token);
    List<LexerToken> getTokens();
    void setAssociatedObject(Object object);
    Object getAssociatedObject();
}
