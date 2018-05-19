package org.jdbc.dynsql.reflection;

import java.util.*;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;
import org.jdbc.dynsql.exception.TemplateCommandException;
import org.jdbc.dynsql.reflection.BuildIterator;
import org.junit.Assert;
import org.junit.Test;

public class TestBuildIterator {

    @Test(expected = TemplateCommandException.class)
    public void TestShouldNotAcceptNullObjects_ExpectedException() throws TemplateCommandException {
        // when
        BuildIterator.getIterator(null);
    }

    @Test
    public void TestGetIteratorForArray_ExpectedGivenIteratorForArray() throws TemplateCommandException {
        // given
        String arrayString[] = {};

        // when
        Iterator<Object> iterator = BuildIterator.getIterator(arrayString);

        // then
        Assert.assertNotNull(iterator);
    }

    @Test
    public void TestGetIteratorForArray_ExpectedGivenIteratorForArrayWithProperValues() throws TemplateCommandException {
        // given
        String arrayString[] = {"1", "2"};

        // when
        Iterator<Object> iterator = BuildIterator.getIterator(arrayString);

        // then
        for(int i = 0; iterator.hasNext(); i++)
            Assert.assertEquals(iterator.next(), arrayString[i]);
    }
    
    @Test
    public void TestGetIteratorForList_ExpectedGivenIteratorForList() throws TemplateCommandException {
        // given
        List<String> listString = new ArrayList<>();

        // when
        Iterator<Object> iterator = BuildIterator.getIterator(listString);

        // then
        Assert.assertNotNull(iterator);
    }

    @Test
    public void TestGetIteratorForList_ExpectedGivenIteratorForListWithProperValues() throws TemplateCommandException {
        // given
        List<String> listString = Arrays.asList("1", "2");

        // when
        Iterator<Object> iterator = BuildIterator.getIterator(listString);

        // then
        for(int i = 0; iterator.hasNext(); i++)
            Assert.assertEquals(iterator.next(), listString.get(i));
    }

    @Test
    public void TestGetIteratorForIterator_ExpectedGivenIteratorForIterator() throws TemplateCommandException {
        // given
        Iterator<Object> createdIterator = new ArrayList<>().iterator();

        // when
        Iterator<Object> iterator = BuildIterator.getIterator(createdIterator);

        // then
        Assert.assertNotNull(iterator);
    }

    @Test
    public void TestGetIteratorForIterator_ExpectedGivenIteratorForIteratorWithProperValues() throws TemplateCommandException {
        // given
        Iterator<String> createdIterator = Arrays.asList("1", "2").iterator();
        Iterator<String> createdIteratorForEqualityTest = Arrays.asList("1", "2").iterator();

        // when
        Iterator<Object> iterator = BuildIterator.getIterator(createdIterator);

        // then
        while(iterator.hasNext())
            Assert.assertEquals(iterator.next(), createdIteratorForEqualityTest.next());
    }

    @Test
    public void TestGetIteratorForCollection_ExpectedGivenIteratorForCollection() throws TemplateCommandException {
        // given
        Collection<Object> collection = new ArrayList<>();

        // when
        Iterator<Object> iterator = BuildIterator.getIterator(collection);

        // then
        Assert.assertNotNull(iterator);
    }

    @Test
    public void TestGetIteratorForCollection_ExpectedGivenIteratorForCollectionWithProperValues() throws TemplateCommandException {
        // given
        Collection<String> collection = Arrays.asList("1", "2");

        // when
        Iterator<Object> iterator = BuildIterator.getIterator(collection);

        // then
        for(int i = 0; iterator.hasNext(); i++)
            Assert.assertEquals(iterator.next(), collection.toArray()[i]);
    }

    @Test
    public void TestGetIteratorForIterable_ExpectedGivenIteratorForIterable() throws TemplateCommandException {
        // given
        Iterable<Object> iterable = new ArrayList<>();

        // when
        Iterator<Object> iterator = BuildIterator.getIterator(iterable);

        // then
        Assert.assertNotNull(iterator);
    }

    @Test
    public void TestGetIteratorForIterable_ExpectedGivenIteratorForIterableWithProperValues() throws TemplateCommandException {
        // given
        Iterable<String> iterable = Arrays.asList("1", "2");
        Iterable<String> iterableForEqualityTest = Arrays.asList("1", "2");

        // when
        Iterator<Object> iterator = BuildIterator.getIterator(iterable);

        // then
        for(int i = 0; iterator.hasNext(); i++)
            Assert.assertEquals(iterator.next(), ((List<String>) iterableForEqualityTest).toArray()[i]);
    }
}
