package org.jdbc.dynsql.lexer;

import org.jdbc.dynsql.lexer.token.LexerToken;
import org.jdbc.dynsql.lexer.token.impl.LexerTokenCommand;
import org.jdbc.dynsql.lexer.token.impl.LexerTokenExpression;
import org.junit.Assert;
import org.junit.Test;

public class TestTemplateLexer {

    private final String FOR_COMMAND = "--FOR row IN rows";
    private final String FOR_COMMAND_WITH_LEADING_SPACE = " --FOR row IN rows";
    private final String SELECT_EXPRESSION = "SELECT * FROM ${table_name}";
    private final String EXPRESSION_CONCATENATION = "'${zoo.horse.name}+${zoo.horse.name}'";
    private final String COMPLEX_SELECT_EXPRESSION = "SELECT * FROM ${table_name} WHERE name LIKE '${zoo.horse.name}+${o}'";

    @Test
    public void whenGivenCommandFor_ExpectedTokenCommand() {
        // given
        Lexer lexer = new Lexer();
        lexer.setTemplate(FOR_COMMAND);

        // when
        LexerToken token = lexer.nextToken();

        // then
        Assert.assertTrue(token instanceof LexerTokenCommand);
    }

    @Test
    public void whenGivenCommandForWithSpace_ExpectedTokenCommand() {
        // given
        Lexer lexer = new Lexer();
        lexer.setTemplate(FOR_COMMAND_WITH_LEADING_SPACE);

        // when
        LexerToken token = lexer.nextToken();

        // then
        Assert.assertTrue(token instanceof LexerTokenCommand);
    }

    @Test
    public void whenGivenCommandFor_ExpectedTokenCommandWithExpression() {
        // given
        Lexer lexer = new Lexer();
        lexer.setTemplate(FOR_COMMAND);

        // when
        LexerToken token = lexer.nextToken();

        // then
        Assert.assertEquals(FOR_COMMAND, token.getExpression());
    }

    @Test
    public void whenGivenSqlWithExpression_ExpectedTokenExpression0() {
        // given
        Lexer lexer = new Lexer();
        lexer.setTemplate(SELECT_EXPRESSION);

        // when
        lexer.nextToken();
        LexerToken token = lexer.nextToken();

        //then
        Assert.assertTrue(token instanceof LexerTokenExpression);
    }

    @Test
    public void whenGivenSqlWithExpression_ExpectedTokenExpressionWithExpression() {
        // given
        Lexer lexer = new Lexer();
        lexer.setTemplate(SELECT_EXPRESSION);

        // when
        lexer.nextToken();
        LexerToken token = lexer.nextToken();

        // then
        Assert.assertEquals("${table_name}", token.getExpression());
    }

    @Test
    public void whenGivenMultilineTemplate_ExpectedTheSameTemplate1() {
        // given
        Lexer lexer = new Lexer();
        lexer.setTemplate(EXPRESSION_CONCATENATION);
        StringBuilder builder = new StringBuilder();

        // when
        while (lexer.hasMoreTokens())
            builder.append(lexer.nextToken().getExpression());

        // then
        Assert.assertEquals(EXPRESSION_CONCATENATION, builder.toString().trim());
    }

    @Test
    public void whenGivenMultilineTemplate_ExpectedTheSameTemplate2() {
        // given
        Lexer lexer = new Lexer();
        String template = COMPLEX_SELECT_EXPRESSION + "\n" + COMPLEX_SELECT_EXPRESSION;
        lexer.setTemplate(template);
        StringBuilder builder = new StringBuilder();

        // when
        while (lexer.hasMoreTokens())
            builder.append(lexer.nextToken().getExpression());

        // then
        Assert.assertEquals(template, builder.toString().trim());
    }
}
