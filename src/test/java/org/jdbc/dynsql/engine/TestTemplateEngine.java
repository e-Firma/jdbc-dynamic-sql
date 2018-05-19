package org.jdbc.dynsql.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final String TEMPLATE_EXAMPLE = "template/example.sql";
    private final String TEMPLATE_FOR = "template/for.sql";

    private List<Animal> horsesList = new ArrayList<>();
    private List<Animal> doggiesList = new ArrayList<>();


    @Before
    public void setUP() {
        prepareHorsesList();
        prepareDoggiesList();
    }

    @Test(expected = TemplateLoadException.class)
    public void TestLoadUnknownTemplateSQL_ExpectedTemplateLoadException() throws TemplateLoadException {
        // given
        TemplateEngine engine = new TemplateEngine();

        // when
        engine.load("template/unknown.sql");
    }

    @Test
    public void TestLoadTemplate_ExpectedFoundFourSectionNumber() throws TemplateLoadException {
        // given
        TemplateEngine engine = new TemplateEngine();

        // when
        engine.load(TEMPLATE_EXAMPLE);

        // then
        Assert.assertEquals(4, engine.getSectionNames().size());
    }

    @Test
    public void TestGetFirstSection_ExpectedUnnamedSectionName() throws TemplateLoadException {
        // given
        TemplateEngine engine = new TemplateEngine();

        // when
        engine.load(TEMPLATE_EXAMPLE);

        // then
        Assert.assertEquals("##", engine.getSectionNames().stream().findFirst().orElse(null));
    }

    @Test
    public void TestGetUnnamedSection_ExpectedSelect1() throws TemplateLoadException, TemplateException {
        // given
        TemplateEngine engine = new TemplateEngine();

        // when
        engine.load(TEMPLATE_EXAMPLE);
        String template = engine.getTemplate("##");

        // then
        Assert.assertTrue(template.contains("SELECT 1"));
    }

    @Test(expected = TemplateException.class)
    public void TestGetUnknownSection_ExpectedTemplateException() throws TemplateLoadException, TemplateException {
        // given
        TemplateEngine engine = new TemplateEngine();

        // when
        engine.load(TEMPLATE_EXAMPLE);
        engine.getTemplate("unknown");
    }

    @Test
    public void TestGetFirstQueryFromTemplate_ExpectedSelect2() throws TemplateLoadException, TemplateException {
        // given
        TemplateEngine engine = new TemplateEngine();

        // when
        engine.load(TEMPLATE_EXAMPLE);
        String template = engine.getTemplate("First SQL Section Name");

        // then
        Assert.assertEquals("-- First SQL Section Name\nSELECT 2", template.trim());
    }

    @Test
    public void TestGetAllQueryInOneString_ExpectedGivenCompleteQueryFromAllSection() throws TemplateLoadException {
        // given
        TemplateEngine engine = new TemplateEngine();

        // when
        engine.load(TEMPLATE_EXAMPLE);
        String template = engine.getTemplate();

        // then
        Assert.assertTrue(template.length() > 0);
    }

    @Test
    public void TestGetZooQueryAndProcess_ExpectedGivenReadyQueryForRun() throws TemplateLoadException, TemplateException, TemplateTranslateException, TemplateCommandException {
        // given
        Animal animal = createAnimal("Horse", 8);
        ZOO zoo = new ZOO();
        zoo.setHorse(animal);

        Map<String, Object> data = new HashMap<>();
        data.put("zoo", zoo);

        // when
        TemplateEngine engine = new TemplateEngine();
        engine.load(TEMPLATE_EXAMPLE);
        String query = engine.process("ZOO SQL", data);

        // then
        Assert.assertEquals("-- ZOO SQL\nSELECT * FROM Horse WHERE age > 8-5 and age < 8+5", query.trim());
    }

    @Test
    public void TestPutForQueryAndProcess_ExpectedGivenReadyQueryForRun() throws TemplateLoadException, TemplateException, TemplateTranslateException, TemplateCommandException
    {
        // given
        Map<String, Object> data = new HashMap<>();
        data.put("horses", horsesList);

        // when
        TemplateEngine engine = new TemplateEngine();
        engine.load(TEMPLATE_FOR);
        String query = engine.process("Section0", data);

        // then
        Assert.assertEquals("-- Section0\nINSERT INTO horses VALUES ('Luna1', 8);\nINSERT INTO horses VALUES ('Luna2', 9);", query.trim());
    }

    @Test
    public void TestPutForInForQueryAndProcess_ExpectedGivenReadyQueryForRun() throws TemplateLoadException, TemplateException, TemplateTranslateException, TemplateCommandException
    {
        // given
        Map<String, Object> data = new HashMap<>();
        data.put("horses", horsesList);
        data.put("doggies", doggiesList);

        // when
        TemplateEngine engine = new TemplateEngine();
        engine.load(TEMPLATE_FOR);
        String query = engine.process("Section1", data);

        // then
        Assert.assertEquals("-- Section1\nINSERT INTO horses VALUES ('Luna1', 8);\nINSERT INTO home VALUES ('Burek1', 'Luna1');\nINSERT INTO home VALUES ('Reksio1', 'Luna1');\nINSERT INTO horses VALUES ('Luna2', 9);\nINSERT INTO home VALUES ('Burek1', 'Luna2');\nINSERT INTO home VALUES ('Reksio1', 'Luna2');", query.trim());
    }

    @Test(expected = TemplateCommandException.class)
    public void TestPutForInForQueryWithNullAndProcess_ExpectedGivenReadyQueryForRun() throws TemplateLoadException, TemplateException, TemplateTranslateException, TemplateCommandException
    {
        // given
        Map<String, Object> data = new HashMap<>();
        data.put("horses", null);

        // when
        TemplateEngine engine = new TemplateEngine();
        engine.load(TEMPLATE_FOR);
        engine.process("Section0", data);
    }

    @Test
    public void TestPutForInForQueryWithEmptyCollectionAndProcess_ExpectedGivenReadyQueryForRun() throws TemplateLoadException, TemplateException, TemplateTranslateException, TemplateCommandException
    {
        // given
        Map<String, Object> data = new HashMap<>();
        data.put("horses", new String[] {});

        // when
        TemplateEngine engine = new TemplateEngine();
        engine.load(TEMPLATE_FOR);
        String query = engine.process("Section0", data);

        // then
        Assert.assertEquals("-- Section0", query.trim());
    }

    // Is this test really necessary?
    @Test
    public void TestSelectAndForWithData_ExpectGivenReadyQueryForRun() throws TemplateLoadException, TemplateException, TemplateTranslateException, TemplateCommandException
    {
        // given
        Map<String, Object> data = new HashMap<>();
        data.put("horses", horsesList);
        data.put("tab_name", "tab_name");

        // when
        TemplateEngine engine = new TemplateEngine();
        engine.load(TEMPLATE_FOR);
        String query = engine.process("Section2", data);

        // then
        Assert.assertEquals("-- Section2\nSELECT * FROM tab_name;\nINSERT INTO horses VALUES ('Luna1', 8);\nINSERT INTO horses VALUES ('Luna2', 9);", query.trim());

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
        Animal animal = new Animal();
        animal.setName(name);
        animal.setAge(age);
        return animal;
    }
}
