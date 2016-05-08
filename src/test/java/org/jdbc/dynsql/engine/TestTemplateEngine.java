package org.jdbc.dynsql.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdbc.dynsql.engine.TemplateEngine;
import org.jdbc.dynsql.exception.TemplateCommandException;
import org.jdbc.dynsql.exception.TemplateException;
import org.jdbc.dynsql.exception.TemplateLoadException;
import org.jdbc.dynsql.exception.TemplateTranslateException;
import org.jdbc.dynsql.test.Animal;
import org.jdbc.dynsql.test.ZOO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestTemplateEngine {

    List<Animal> horsesList = new ArrayList<Animal>();
    List<Animal> doggiesList = new ArrayList<Animal>();

    @Before
    public void setUP() {
        prepareHorsesList();
        prepareDoggiesList();
    }

    @Test(expected = TemplateLoadException.class)
    public void TestLoadUnknownTemplateSQL_ExpectedTemplateLoadException() throws TemplateLoadException {
        TemplateEngine engine = new TemplateEngine();
        engine.Load("template/unknown.sql");
    }

    @Test
    public void TestLoadTemplate_ExpectedFoundFourSectionNumber() throws TemplateLoadException {
        // given
        TemplateEngine engine = new TemplateEngine();
        engine.Load("template/example.sql");
        
        // then
        Assert.assertEquals(4, engine.getSectionNames().size());
    }

    @Test
    public void TestGetFirstSection_ExpectedUnnamedSectionName() throws TemplateLoadException {
        TemplateEngine engine = new TemplateEngine();
        engine.Load("template/example.sql");
        String firstSectionName = engine.getSectionNames().iterator().next();
        Assert.assertEquals("##", firstSectionName);
    }

    @Test
    public void TestGetUnnamedSection_ExpectedSelect1() throws TemplateLoadException, TemplateException {
        TemplateEngine engine = new TemplateEngine();
        engine.Load("template/example.sql");
        String template = engine.getTemplate("##");
        Assert.assertEquals(template.contains("SELECT 1"), true);
    }

    @Test(expected = TemplateException.class)
    public void TestGetUnknownSection_ExpectedTemplateException() throws TemplateLoadException, TemplateException {
        TemplateEngine engine = new TemplateEngine();
        engine.Load("template/example.sql");
        engine.getTemplate("unknown");
    }

    @Test
    public void TestGetFirstQueryFromTemplate_ExpectedSelect2() throws TemplateLoadException, TemplateException {
        TemplateEngine engine = new TemplateEngine();
        engine.Load("template/example.sql");
        String template = engine.getTemplate("First SQL Section Name");
        Assert.assertEquals("-- First SQL Section Name\nSELECT 2", template.trim());
    }

    @Test
    public void TestGetAllQueryInOneString_ExpectedGivenCompleteQueryFromAllSection() throws TemplateLoadException, TemplateException {
        TemplateEngine engine = new TemplateEngine();
        engine.Load("template/example.sql");
        String template = engine.getTemplate();
        Assert.assertTrue(template.length() > 0);
    }

    @Test
    public void TestGetZooQueryAndProcess_ExpectedGivenReadyQueryForRun() throws TemplateLoadException, TemplateException, TemplateTranslateException, TemplateCommandException {
        // given
        Animal animal = createAnimal("Horse", 8);
        ZOO zoo = new ZOO();
        zoo.setHorse(animal);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("zoo", zoo);

        // when
        TemplateEngine engine = new TemplateEngine();
        engine.Load("template/example.sql");
        String query = engine.process("ZOO SQL", data);
        
        // then
        Assert.assertEquals("-- ZOO SQL\nSELECT * FROM Horse WHERE age > 8-5 and age < 8+5", query.trim());
    }
    
    @Test
    public void TestPutForQueryAndProcess_ExpectedGivenReadyQueryForRun() throws TemplateLoadException, TemplateException, TemplateTranslateException, TemplateCommandException
    {
        // given
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("horses", horsesList);
        
        // when
        TemplateEngine engine = new TemplateEngine();
        engine.Load("template/for.sql");
        String query = engine.process("Section0", data);
        // then
        Assert.assertEquals("-- Section0\nINSERT INTO horses VALUES ('Luna1', 8);\nINSERT INTO horses VALUES ('Luna2', 9);", query.trim());
    }
    
    @Test
    public void TestPutForInForQueryAndProcess_ExpectedGivenReadyQueryForRun() throws TemplateLoadException, TemplateException, TemplateTranslateException, TemplateCommandException
    {
        // given
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("horses", horsesList);
        data.put("doggies", doggiesList);
        
        // when
        TemplateEngine engine = new TemplateEngine();
        engine.Load("template/for.sql");
        String query = engine.process("Section1", data);
        // then
        
        Assert.assertEquals("-- Section1\nINSERT INTO horses VALUES ('Luna1', 8);\nINSERT INTO home VALUES ('Burek1', 'Luna1');\nINSERT INTO home VALUES ('Reksio1', 'Luna1');\nINSERT INTO horses VALUES ('Luna2', 9);\nINSERT INTO home VALUES ('Burek1', 'Luna2');\nINSERT INTO home VALUES ('Reksio1', 'Luna2');", query.trim());
    }
    
    @Test(expected = TemplateCommandException.class)
    public void TestPutForInForQueryWithNullAndProcess_ExpectedGivenReadyQueryForRun() throws TemplateLoadException, TemplateException, TemplateTranslateException, TemplateCommandException
    {
        // given
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("horses", null);
        
        // when
        TemplateEngine engine = new TemplateEngine();
        engine.Load("template/for.sql");
        engine.process("Section0", data);
    }
    
    @Test
    public void TestPutForInForQueryWithEmptyCollectionAndProcess_ExpectedGivenReadyQueryForRun() throws TemplateLoadException, TemplateException, TemplateTranslateException, TemplateCommandException
    {
        // given
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("horses", new String[] {});
        
        // when
        TemplateEngine engine = new TemplateEngine();
        engine.Load("template/for.sql");
        String query = engine.process("Section0", data);
        // then
        Assert.assertEquals("-- Section0", query.trim());
    }
    
    private void prepareHorsesList()
    {
        horsesList.add(createAnimal("Luna1", 8));
        horsesList.add(createAnimal("Luna2", 9));
    }
    
    private void prepareDoggiesList()
    {
        doggiesList.add(createAnimal("Burek1", 15));
        doggiesList.add(createAnimal("Reksio1", 40));
    }
    
    private Animal createAnimal(String name, int age)
    {
        Animal horse = new Animal();
        horse.setName(name);
        horse.setAge(age);
        return horse;
    }
}
