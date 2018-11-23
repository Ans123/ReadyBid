package net.readybid.app.entities.rfp;

import net.readybid.entities.Id;

public class LetterTemplateImpl implements LetterTemplate {

    private Id id;
    private String template;

    @Override
    public String getTemplate() {
        return template;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
