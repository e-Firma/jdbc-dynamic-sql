package org.jdbc.dynsql.engine;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang.StringEscapeUtils;
import org.jdbc.dynsql.exception.TemplateCommandException;
import org.jdbc.dynsql.exception.TemplateException;
import org.jdbc.dynsql.exception.TemplateTranslateException;
import org.jdbc.dynsql.lexer.token.LexerToken;
import org.jdbc.dynsql.lexer.token.impl.LexerTokenCommand;
import org.jdbc.dynsql.lexer.token.impl.LexerTokenExpression;
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
                    associatedObject = getValueExpression(new LexerTokenExpression(command.getForCollectionName()), data);
                    iterator = BuildIterator.getIterator(associatedObject);
                    command.setAssociatedObject(iterator);
                    if (iterator.hasNext()) {
                        commandStack.push(command);
                        Object nextObject = iterator.next();
                        data.put(command.getForVariableName(), nextObject);
                        partSQL.append(process(command.getTokens(), data));
                    }
                    break;
                case END_FOR:
                    LexerTokenCommand commandFor = commandStack.peek();
                    iterator = (Iterator<Object>) commandFor.getAssociatedObject();
                    if (iterator.hasNext()) {
                        Object nextObject = iterator.next();
                        data.put(commandFor.getForVariableName(), nextObject);
                        index = 0;
                    } else {
                        commandStack.pop();
                    }
                    break;
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
    	String objectPath[] = token.getTokenComponents();
        Object extractedObject = data.get(objectPath[0]);
        if(objectPath.length == 1) {
        	return extractedObject;
        }
        else {
        	objectPath = Arrays.copyOfRange(objectPath, 1, objectPath.length);
        	return actualExpressionEvaluation(extractedObject, objectPath);
        }
    }
    
    private Object actualExpressionEvaluation(Object data, String[] objectPath) throws TemplateTranslateException {
    	try {
            if (data instanceof Map<?, ?>) {
            	return getValueExpressionFromMap((Map<String, Object>) data, objectPath);
            } else {
            	return getValueExpressionFromClassObject(data, objectPath);
            }
        } catch (Exception ex) {
            throw new TemplateTranslateException(ex);
        }
    }
    
    private Object getValueExpressionFromMap(Map<String, Object> data, String[] objectPath) throws TemplateTranslateException {
    	String objectName = objectPath[0];
    	Object object = data.get(objectName);
    	if (objectPath.length > 1) {
			objectPath = Arrays.copyOfRange(objectPath, 1, objectPath.length);
			return actualExpressionEvaluation(data, objectPath);
		}
		return object;
	}

	public Object getValueExpressionFromClassObject(Object data, String[] objectPath) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, TemplateTranslateException {
		String fieldName = objectPath[0];
		Field field = data.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		data = field.get(data);
		if (objectPath.length > 1) {
			objectPath = Arrays.copyOfRange(objectPath, 1, objectPath.length);
			return actualExpressionEvaluation(data, objectPath);
		}
		return data;
	}

    public String escape(Object value) {
        if ( value == null)
            return "null";
        return StringEscapeUtils.escapeSql(value.toString());
    }
}
