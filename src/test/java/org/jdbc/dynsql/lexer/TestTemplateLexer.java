package org.jdbc.dynsql.lexer;

import java.util.ArrayList;
import java.util.List;

import org.jdbc.dynsql.lexer.Lexer;
import org.jdbc.dynsql.lexer.token.LexerToken;
import org.jdbc.dynsql.lexer.token.impl.LexerTokenCommand;
import org.jdbc.dynsql.lexer.token.impl.LexerTokenExpression;
import org.junit.Assert;
import org.junit.Test;

public class TestTemplateLexer {

    @Test
    public void whenGivenCommandFor_ExpectedTokenCommand() {
        Lexer lexer = new Lexer();
        lexer.setTemplate("--FOR row IN rows");
        LexerToken token = lexer.nextToken();
        Assert.assertTrue(token instanceof LexerTokenCommand);
    }


    @Test
    public void whenGivenCommandFor_ExpectedTokenCommandWithExpression() {
        String templateFOR = "--FOR row IN rows";
        Lexer lexer = new Lexer();
        lexer.setTemplate(templateFOR);
        LexerToken token = lexer.nextToken();
        Assert.assertEquals(token.getExpression(), templateFOR);
    }

    @Test
    public void whenGivenSqlWithExpression_ExpectedTokenExpression0() {
        Lexer lexer = new Lexer();
        lexer.setTemplate("SELECT * FROM ${table_name}");
        LexerToken token = lexer.nextToken();
        token = lexer.nextToken();
        Assert.assertTrue(token instanceof LexerTokenExpression);
    }
    
    @Test
    public void whenGivenSqlWithExpression_ExpectedTokenExpressionWithExpression() {
        Lexer lexer = new Lexer();
        lexer.setTemplate("SELECT * FROM ${table_name}");
        LexerToken token = lexer.nextToken();
        token = lexer.nextToken();
        Assert.assertEquals(token.getExpression(), "${table_name}");
    }

    @Test
    public void whenGivenMultilineTemplate_ExpectedThesameTemplate1() {
        String template = "'${zoo.horse.name}+${zoo.horse.name}'";
        
        Lexer lexer = new Lexer();
        lexer.setTemplate(template);
        StringBuilder builder = new StringBuilder();
        while (lexer.hasMoreTokens()) {
            LexerToken token = lexer.nextToken();
            builder.append(token.getExpression());
        }
        String result = builder.toString().trim();
        Assert.assertEquals(template, result);
    }

    @Test
    public void whenGivenMultilineTemplate_ExpectedThesameTemplate2() {
        List<String> sql = new ArrayList<String>();
        String template = "SELECT * FROM ${table_name} WHERE name LIKE '${zoo.horse.name}+${o}'";
        template = template + "\n" + template;
        
        Lexer lexer = new Lexer();
        lexer.setTemplate(template);
        StringBuilder builder = new StringBuilder();
        while (lexer.hasMoreTokens()) {
            LexerToken token = lexer.nextToken();
            builder.append(token.getExpression());
        }
        String result = builder.toString().trim();
        Assert.assertEquals(template, result);
    }
    
    @Test
    public void whenGivenMultilineTemplate_ExpectedThesameTemplate3() {
        Lexer lexer = new Lexer();
        List<String> sql = new ArrayList<String>();
        String template = "SELECT * FROM ${table_name} WHERE name LIKE '${zoo.horse.name}'+${o}";
        template = template + "\n" + template;
        lexer.setTemplate(template);
        StringBuilder builder = new StringBuilder();
        while (lexer.hasMoreTokens()) {
            LexerToken token = lexer.nextToken();
            builder.append(token.getExpression());
        }
        String result = builder.toString().trim();
        Assert.assertEquals(template, result);
    }
    
}
