package ru.mipt.hometask.strings.interfaces;

import ru.mipt.hometask.strings.exceptions.TemplateAlreadyExist;

@FunctionalInterface
public interface IMetaTemplateMatcherFactory {
    public IMetaTemplateMatcher generate() throws TemplateAlreadyExist;
}

