package ru.mipt.hometask.strings;

import ru.mipt.hometask.strings.exceptions.TemplateAlreadyExist;
import ru.mipt.hometask.strings.interfaces.ICharStream;
import ru.mipt.hometask.strings.interfaces.IMetaTemplateMatcher;

import java.util.ArrayList;
import java.util.List;

public class DynamicTemplateMatcher implements IMetaTemplateMatcher {
    List<AhoTrie> tries = new ArrayList<>();
    int templatesCount = 0;

    @Override
    public int addTemplate(String template) throws TemplateAlreadyExist {
        for (Trie item : tries) {
            if (item != null) {
                if (item.search(template) != null) {
                    throw new TemplateAlreadyExist();
                }
            }
        }
        AhoTrie buffer = new AhoTrie();
        int id = templatesCount++;
        buffer.addString(template, id);
        int index = 0;
        while (index < tries.size() && tries.get(index) != null) {
            buffer.merge(tries.get(index));
            tries.set(index, null);
            ++index;
        }
        if (index < tries.size()) {
            tries.set(index, buffer);
        } else {
            tries.add(buffer);
        }
        return id;
    }

    @Override
    public List<Occurence> matchStream(ICharStream stream) {
        return AhoTrie.searchSubstrings(stream, tries);
    }
}
