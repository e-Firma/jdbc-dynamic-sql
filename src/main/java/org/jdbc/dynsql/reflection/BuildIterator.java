package org.jdbc.dynsql.reflection;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections4.iterators.ArrayIterator;
import org.jdbc.dynsql.exception.TemplateCommandException;

public class BuildIterator {

    /**
     * Creates an Iterator of provided object (if possible). Currently supported types:<br>
     * <b>Array</b><br>
     * <b>List</b><br>
     * <b>Iterator</b> - <b>IMPORTANT!</b> - will return the same iterator!<br>
     * <b>Collection</b><br>
     * <b>Iterable</b><br>
     * @param object from this object an iterator will be returned
     * @return Iterator&lt;Object&gt; - an iterator
     * @throws TemplateCommandException will throw exception in case of <b>null</b> or <b>unsupported type</b>
     */
    @SuppressWarnings("unchecked")
    public static Iterator<Object> getIterator(Object object) throws TemplateCommandException {
    	if ( object == null)
    		throw new TemplateCommandException("Can't iterate over the null object");
        if (object.getClass().isArray()) {
            return new ArrayIterator<>(object);
        }
        if (object instanceof Iterator) {
            return (Iterator<Object>) object;
        }
        if (object instanceof Collection || object instanceof Iterable){
            return ((Collection<Object>) object).iterator();
        }
        else
        {
            throw new TemplateCommandException("Object <" + object.toString() + "> is not collection or array.");
        }
    }
}
