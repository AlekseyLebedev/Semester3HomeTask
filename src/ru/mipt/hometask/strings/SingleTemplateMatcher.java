package ru.mipt.hometask.strings;

import ru.mipt.hometask.strings.exceptions.EmptyStreamException;
import ru.mipt.hometask.strings.exceptions.TemplateAlreadyExist;
import ru.mipt.hometask.strings.interfaces.ICharStream;
import ru.mipt.hometask.strings.interfaces.IMetaTemplateMatcher;

import java.util.ArrayList;
import java.util.List;

public final class SingleTemplateMatcher implements IMetaTemplateMatcher {
    public static final int BEGIN_PREFIX_COUNTED = 1;
    private String template;
    private List<Character> appended = new ArrayList<>();
    private List<Character> prepended = new ArrayList<>();
    private int prefixCounted = BEGIN_PREFIX_COUNTED;
    private List<Integer> prefixFunc;

    private char getCharAt(int i) {
        int prependedSize = prepended.size();
        if (i < prependedSize) {
            return prepended.get(prependedSize - i - 1);
        } else {
            int mainLength = template.length();
            if (i < mainLength + prependedSize) {
                return template.charAt(i - prependedSize);
            } else {
                return appended.get(i - mainLength - prependedSize);
            }
        }
    }

    private boolean equalsChars(int index, char symbol) {
        if (index == templateSize()) {
            return false;
        } else {
            return getCharAt(index) == symbol;
        }
    }

    private int templateSize() {
        return prepended.size() + appended.size() + template.length();
    }

    private int prefixFunction(int index) {
        if (index < prefixCounted) {
            return prefixFunc.get(index);
        } else {
            int value = prefixFunc.get(prefixCounted - 1);
            while (prefixCounted <= index) {
                while (value > 0 && !equalsChars(prefixCounted, getCharAt(value))) {
                    value = prefixFunc.get(value - 1);
                }
                if (equalsChars(prefixCounted, getCharAt(value))) {
                    ++value;
                }
                if (prefixCounted < prefixFunc.size()) {
                    prefixFunc.set(prefixCounted, value);
                } else {
                    prefixFunc.add(value);
                }
                ++prefixCounted;
            }
            return value;
        }
    }

    @Override
    public int addTemplate(String template) throws TemplateAlreadyExist {
        if (this.template != null) {
            throw new UnsupportedOperationException("Only one template is supported");
        }
        this.template = template;
        prefixFunc = new ArrayList<>(template.length());
        prefixFunc.add(0);
        return 0;
    }

    @Override
    public List<Occurence> matchStream(ICharStream stream) {
        if (template == null) {
            throw new UnsupportedOperationException("There is no template assigned");
        }
        List<Occurence> result = new ArrayList<>();
        int value = 0;
        int index = 0;
        int templateSize = templateSize();
        try {
            while (!stream.isEmpty()) {
                char symbol = stream.getChar();
                while (value > 0 && !equalsChars(value, symbol)) {
                    value = prefixFunction(value - 1);
                }
                if (equalsChars(value, symbol)) {
                    ++value;
                }
                ++index;
                if (value == templateSize) {
                    if (templateSize == 0) {
                        result.add(new Occurence(index - 1, 0));
                    } else {
                        result.add(new Occurence(index - templateSize, 0));
                    }
                }
            }
        } catch (EmptyStreamException e) {
            throw new RuntimeException(Trie.ERROR_STREAM_MESSAGE, e);
        }
        return result;
    }

    public void appendCharToTemplate(char symbol) {
        appended.add(symbol);
    }

    public void prependCharToTemplate(char symbol) {
        prepended.add(symbol);
        prefixCounted = BEGIN_PREFIX_COUNTED;
    }
}
