package ru.mipt.hometask.strings.interfaces;

import ru.mipt.hometask.strings.exceptions.TemplateAlreadyExist;

@FunctionalInterface
public interface IMetaTemplateMatcherFactory {
    IMetaTemplateMatcher generate() throws TemplateAlreadyExist;
}
