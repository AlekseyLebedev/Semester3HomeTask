package ru.mipt.hometask.strings.interfaces;

import ru.mipt.hometask.strings.Occurence;
import ru.mipt.hometask.strings.exceptions.TemplateAlreadyExist;

import java.util.List;

public interface IMetaTemplateMatcher {
    int addTemplate(final String template) throws TemplateAlreadyExist;

    List<Occurence> matchStream(ICharStream stream);
}
