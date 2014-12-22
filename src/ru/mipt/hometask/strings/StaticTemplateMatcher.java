package ru.mipt.hometask.strings;

import ru.mipt.hometask.strings.exceptions.TemplateAlreadyExist;
import ru.mipt.hometask.strings.interfaces.ICharStream;
import ru.mipt.hometask.strings.interfaces.IMetaTemplateMatcher;

import java.util.List;

public final class StaticTemplateMatcher implements IMetaTemplateMatcher {
    private AhoTrie templateTrie = new AhoTrie();
    private int templatesCount = 0;
    private boolean wasStreamMatched = false;

    private int getCurrentTemplateId() {
        return templatesCount++;
    }

    @Override
    public int addTemplate(String template) throws TemplateAlreadyExist {
        if (wasStreamMatched) {
            throw new IllegalStateException("All templates have to be added before first stream matching");
        }
        int templateId = getCurrentTemplateId();
        if (templateTrie.addString(template, templateId)) {
            return templateId;
        } else {
            throw new TemplateAlreadyExist("Template \"" + template + "\" has been already added");
        }
    }

    @Override
    public List<Occurence> matchStream(ICharStream stream) {
        wasStreamMatched = true;
        return templateTrie.searchSubstrings(stream);
    }
}
