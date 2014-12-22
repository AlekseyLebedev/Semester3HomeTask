package ru.mipt.hometask.strings;

public class Occurence implements Comparable {
    private int position;
    private String template;
    private int templateId;

    public Occurence(int position, String template, int templateId) {
        this(position, templateId);
        this.template = template;
    }

    public Occurence(int position, int templateId) {
        this.position = position;
        this.templateId = templateId;
    }

    public int getPosition() {
        return position;
    }

    public int getTemplateId() {
        return templateId;
    }

    public String getTemplate() {
        if (template == null) {
            throw new UnsupportedOperationException("Matcher hasn't provided template as string");
        }
        return template;
    }

    public boolean isTemplateStringEnabled() {
        return template != null;
    }

    @Override
    public int compareTo(Object o) {
        Occurence other = (Occurence) o;
        if (other.getPosition() == getPosition()) {
            return this.getTemplateId() - other.getTemplateId();
        } else {
            return this.getPosition() - other.getPosition();
        }
    }
}
