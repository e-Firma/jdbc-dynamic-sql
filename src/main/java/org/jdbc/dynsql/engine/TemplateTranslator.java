package org.jdbc.dynsql.engine;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang.StringEscapeUtils;
import org.jdbc.dynsql.exception.TemplateCommandException;
import org.jdbc.dynsql.exception.TemplateException;
import org.jdbc.dynsql.exception.TemplateTranslateException;
import org.jdbc.dynsql.lexer.LexerToken;
import org.jdbc.dynsql.lexer.LexerTokenCommand;
import org.jdbc.dynsql.lexer.LexerTokenExpression;
import org.jdbc.dynsql.reflection.BuildIterator;

public class TemplateTranslator {

    private TemplateParser parser;
    private Stack<LexerTokenCommand> commandStack;

    public TemplateTranslator() {
        commandStack = new Stack<LexerTokenCommand>();
        this.parser = new TemplateParser();
    }

    public String process(String template, Map<String, Object> data) throws TemplateTranslateException, TemplateException, TemplateCommandException {
        LexerToken rootToken = parser.parse(template);
        return process(rootToken.getTokens(), data);
    }

    @SuppressWarnings("unchecked")
	public String process(List<LexerToken> tokens, Map<String, Object> data) throws TemplateTranslateException, TemplateException, TemplateCommandException {

        StringBuilder partSQL = new StringBuilder();
        int index = 0;
        int count = tokens.size();
        Iterator<Object> iterator = null;

        while (index < count) {
            LexerToken token = tokens.get(index++);
            Object value = token.getExpression();
            Object associatedObject = null;

            if (token instanceof LexerTokenExpression) {
                value = escape(getValueExpression(token, data));
                partSQL.append(value);
            } else if (token instanceof LexerTokenCommand) {
                LexerTokenCommand command = (LexerTokenCommand) token;

                switch (command.getCommandName()) {
                case FOR:
                    associatedObject = getValueExpression(new LexerTokenExpression(command.getTokenComponents()[3]), data);
                    iterator = BuildIterator.getIterator(associatedObject);
                    command.setAssociatedObject(iterator);
                    if (iterator.hasNext()) {
                        commandStack.push(command);
                        Object nextObject = iterator.next();
                        data.put(command.getTokenComponents()[1], nextObject);
                        partSQL.append(process(command.getTokens(), data));
                    }
                    break;
                    
                case END_FOR:
                    LexerTokenCommand commandFor = commandStack.peek();
                    iterator = (Iterator<Object>) commandFor.getAssociatedObject();
                    if (iterator.hasNext()) {
                        Object nextObject = iterator.next();
                        data.put(commandFor.getTokenComponents()[1], nextObject);
                        index = 0;
                    } else {
                        commandStack.pop();
                    }
				case END_IF:
					break;
				case IF:
					break;
				default:
					break;
                }

            } else
                partSQL.append(value);
        }
        return partSQL.toString();
    }

    public Object getValueExpression(LexerToken token, Map<String, Object> data) throws TemplateTranslateException {
        try {
            String objectPath[] = token.getTokenComponents();
            Object object = data.get(objectPath[0]);
            int position = 1;
            int size = objectPath.length;
            if (size == 1) {
                return object;
            } else
                while (position < size) {
                    String fieldName = objectPath[position];

                    Field field = object.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    object = field.get(object);
                    position++;
                }
            return object;
        } catch (Exception ex) {
            throw new TemplateTranslateException(ex);
        }
    }

    public String escape(Object value) {
        if ( value == null)
            return "null";
        if (value instanceof String)
        {
            return "'" + StringEscapeUtils.escapeSql(value.toString()) + "'";
        }
        return StringEscapeUtils.escapeSql(value.toString());
    }
}
