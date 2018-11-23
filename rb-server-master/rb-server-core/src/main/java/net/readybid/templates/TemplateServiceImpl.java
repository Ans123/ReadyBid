package net.readybid.templates;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by DejanK on 8/27/2015.
 *
 */
@Service
public class TemplateServiceImpl implements TemplateService {

    @Override
    public String parseTemplate(String templatePath, Map<String, String> model) throws IOException {
        final String template = loadTemplate(templatePath);
        return parseTemplate(template, model, "{%s}");
    }

    private String loadTemplate(String templatePath) throws IOException {
        final Resource resource = new ClassPathResource(templatePath);
        final Scanner s = new Scanner(resource.getInputStream());
        return s.useDelimiter("\\A").hasNext() ? s.next() : "";
    }

    private String parseTemplate(String template, Map<String, String> model, String varFormat) {
        String html = template;
        for(String key: model.keySet()) {
            final String value = model.getOrDefault(key, "");
            html = html.replace(String.format(varFormat, key), value == null ? "" : value);
        }
        return html;
    }

}
