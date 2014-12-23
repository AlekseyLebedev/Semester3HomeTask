package ru.mipt.hometask.strings;

import ru.mipt.hometask.strings.exceptions.TemplateAlreadyExist;
import ru.mipt.hometask.strings.interfaces.ICharStream;
import ru.mipt.hometask.strings.interfaces.IMetaTemplateMatcher;

import java.util.ArrayList;
import java.util.List;

public class WildcardSingleTemplateMatcher implements IMetaTemplateMatcher {
    int[] sizes;
    private String template;
    private List<WildcardTerminateInfo> templatesInfo = new ArrayList<>();
    AhoTrie trie = new AhoTrie() {
        @Override
        protected TerminatingTrieNodeInfo generateTerminatingInfo() {
            WildcardTerminateInfo info = new WildcardTerminateInfo((AhoFinalInfo) super.generateTerminatingInfo());
            templatesInfo.add(info);
            return info;
        }
    };

    @Override
    public int addTemplate(String template) throws TemplateAlreadyExist {
        if (this.template != null) {
            throw new UnsupportedOperationException("Only one template is supported");
        }
        this.template = template;
        int templateId = 0;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < template.length(); i++) {
            char symbol = template.charAt(i);
            if (symbol == '?') {
                templateId = addPart(templateId, builder, i);
            } else {
                builder.append(symbol);
            }
        }
        addPart(templateId, builder, template.length());
        sizes = new int[templatesInfo.size()];
        for (int i = 0; i < sizes.length; i++) {
            int value = 0;
            if (i > 0) {
                value = sizes[i - 1];
            }
            sizes[i] = value + templatesInfo.get(i).deltas.size();
        }
        return 0;
    }

    private int addPart(int templateId, StringBuilder builder, int index) {
        String part = builder.toString();
        builder.setLength(0);
        TerminatingTrieNodeInfo result = trie.search(part);
        if (result == null) {
            trie.addString(part, templateId++);
            result = trie.search(part);
        }
        ((WildcardTerminateInfo) result).deltas.add(index - part.length());
        ((WildcardTerminateInfo) result).isEmpty = (part.length() == 0);
        return templateId;
    }

    @Override
    public List<Occurence> matchStream(ICharStream stream) {
        checkTemplateExsist();
        List<Occurence> result = new ArrayList<>();
        int templatesPartsCount = sizes[sizes.length - 1];
        BooleanMatrix matrix = new BooleanMatrix(templatesPartsCount);
        List<Occurence> occurences = trie.searchSubstrings(stream);
        int length = 0;
        for (Occurence item : occurences) {
            int id = item.getTemplateId();
            int position = item.getPosition();
            if (position > length) {
                length = position;
            }
            List<Integer> deltas = templatesInfo.get(id).deltas;
            for (int i = 0; i < deltas.size(); i++) {
                int x = position - deltas.get(i);
                if (x >= 0) {
                    matrix.set(x, sizes[id] - deltas.size() + i, true);
                }
                if (templatesInfo.get(id).isEmpty) {
                    x++;
                    if (x >= 0) {
                        matrix.set(x, sizes[id] - deltas.size() + i, true);
                    }
                }
            }
        }
        for (int i = 0; i <= length; i++) {
            if (check(templatesPartsCount, matrix, i)) {
                result.add(new Occurence(i, template, 0));
            }
        }
        return result;
    }

    private boolean check(int templatesPartsCount, BooleanMatrix matrix, int i) {
        for (int j = 0; j < templatesPartsCount; j++) {
            if (!matrix.get(i, j)) {
                return false;
            }
        }
        return true;
    }

    private void checkTemplateExsist() {
        if (template == null) {
            throw new UnsupportedOperationException("There is no template assigned");
        }
    }
}

class WildcardTerminateInfo extends AhoFinalInfo {
    List<Integer> deltas = new ArrayList<>();
    boolean isEmpty = false;

    WildcardTerminateInfo(int templateId, String template) {
        super(templateId, template);
    }

    public WildcardTerminateInfo(AhoFinalInfo other) {
        super(other.getTemplateId(), other.getTemplate());
    }
}
