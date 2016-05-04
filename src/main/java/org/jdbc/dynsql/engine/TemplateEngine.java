package org.jdbc.dynsql.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.jdbc.dynsql.exception.TemplateCommandException;
import org.jdbc.dynsql.exception.TemplateException;
import org.jdbc.dynsql.exception.TemplateLoadException;
import org.jdbc.dynsql.exception.TemplateTranslateException;

public class TemplateEngine {

    private Map<String, String> template = new LinkedHashMap<String, String>();

    public boolean Load(String templatePath) throws TemplateLoadException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = null;
        try {
            URI uri = new URI(classLoader.getResource(templatePath).toString());
            file = new File(uri.getPath());
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line = null;
            String keyName = "##";
            StringBuilder body = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("--##")) {
                    if (body != null && keyName != null) {
                        template.put(keyName, body.toString());
                    }
                    keyName = line.replace("--##", "").trim();
                    body = new StringBuilder();
                    body.append("-- " + keyName + "\n");
                } else if (body != null) {
                    body.append(line + "\n");
                }
            }
            reader.close();

            if (body != null && body.length() > 0 && keyName != null) {
                template.put(keyName, body.toString());
            }

        } catch (Exception ex) {
            throw new TemplateLoadException(ex);
        }
        return true;
    }

    public Set<String> getSectionNames() {
        return this.template.keySet();
    }

    public String getTemplate(String sectionName) throws TemplateException {
        String body = template.get(sectionName);
        if (body == null) {
            throw new TemplateException("Not found section name in SQL Template.");
        }
        return body;
    }

    public String getTemplate() {
        StringBuilder template = new StringBuilder();
        for (String bodySection : this.template.values()) {
            template.append(bodySection);
        }
        return template.toString();
    }

    public String process(Map<String, Object> data) throws TemplateTranslateException, TemplateCommandException, TemplateException {
        String queryTemplate = this.getTemplate();

        TemplateTranslator translator = new TemplateTranslator();
        queryTemplate = translator.process(queryTemplate, data);

        return queryTemplate;
    }

    public String process(String sectionName, Map<String, Object> data) throws TemplateException, TemplateTranslateException, TemplateCommandException {
        String queryTemplate = this.getTemplate(sectionName);

        TemplateTranslator translator = new TemplateTranslator();
        queryTemplate = translator.process(queryTemplate, data);

        return queryTemplate;
    }
}
