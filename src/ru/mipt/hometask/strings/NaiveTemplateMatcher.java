package ru.mipt.hometask.strings;

import ru.mipt.hometask.strings.exceptions.EmptyStreamException;
import ru.mipt.hometask.strings.exceptions.TemplateAlreadyExist;
import ru.mipt.hometask.strings.interfaces.ICharStream;
import ru.mipt.hometask.strings.interfaces.IMetaTemplateMatcher;

import java.util.ArrayList;
import java.util.List;

public final class NaiveTemplateMatcher implements IMetaTemplateMatcher {
    private ArrayList<String> templates = new ArrayList<String>();
    private Trie templateTrie = new Trie();
    private int maxTemplateLength = 1;

    private int getCurrentTemplateId() {
        return templates.size() - 1;
    }

    @Override
    public int addTemplate(String template) throws TemplateAlreadyExist {
        if (templateTrie.addString(template)) {
            templates.add(template);
            if (template.length() > maxTemplateLength) {
                maxTemplateLength = template.length();
            }
            return getCurrentTemplateId();
        } else {
            throw new TemplateAlreadyExist("Template \"" + template + "\" has been already added");
        }
    }

    @Override
    public List<Occurence> matchStream(ICharStream stream) {
        List<Occurence> result = new ArrayList<Occurence>();
        char[] lastChars = new char[maxTemplateLength];
        int begin = 0;
        int length = 0;
        int streamLength = 0;
        try {
            while (!stream.isEmpty()) {
                ++streamLength;
                int position;
                if (length == maxTemplateLength) {
                    lastChars[begin++] = stream.getChar();
                    begin %= maxTemplateLength;
                    position = begin;
                } else {
                    lastChars[length++] = stream.getChar();
                    position = length;
                }
                for (int templateIndex = 0; templateIndex < templates.size(); ++templateIndex) {
                    String template = templates.get(templateIndex);
                    if (template.length() == 0) {
                        result.add(new Occurence(streamLength - 1, template, templateIndex));
                    } else {
                        int beginIndex = position - template.length();
                        if (beginIndex < 0) {
                            beginIndex += maxTemplateLength;
                        }
                        if (checkTemplate(lastChars, length, template, beginIndex)) {
                            result.add(new Occurence(streamLength - template.length(), template, templateIndex));
                        }
                    }
                }
            }
        } catch (EmptyStreamException e) {
            throw new RuntimeException(Trie.ERROR_STREAM_MESSAGE, e);
        }
        return result;
    }

    private boolean checkTemplate(char[] lastChars, int length, String template, int beginIndex) {
        for (int i = 0; i < template.length() && i < length; i++) {
            if (lastChars[(beginIndex + i) % maxTemplateLength] != template.charAt(i)) {
                return false;
            }
        }
        return true;
    }
}
