package ru.mipt.hometask.strings;

import ru.mipt.hometask.strings.exceptions.EmptyStreamException;
import ru.mipt.hometask.strings.interfaces.ICharStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AhoTrie extends Trie {
    private int templateId;
    private String template;

    @Override
    protected TrieNode generateNode(char edge, TrieNode parent) {
        return new AhoTrieNode(parent, edge);
    }

    @Override
    protected TerminatingTrieNodeInfo generateTerminatingInfo() {
        return new AhoFinalInfo(templateId, template);
    }

    @Override
    public boolean addString(String input) {
        return super.addString(input);
    }

    @Override
    public boolean addStream(ICharStream stream) {
        if (template == null) {
            throw new IllegalArgumentException("Should be templateId argument");
        }
        boolean result;
        try {
            result = super.addStream(stream);
        } finally {
            template = null;
            templateId = -1;
        }
        return result;
    }

    public boolean addString(String string, int templateId) {
        this.templateId = templateId;
        template = string;
        return addString(string);
    }

    public AhoTrieNode getRootAho() {
        return (AhoTrieNode) getRoot();
    }

    public List<Occurence> searchSubstrings(ICharStream stream) {
        List<Occurence> result = new ArrayList<>();
        AhoTrieNode root = getRootAho();
        root.sufficsLink = root;
        AhoTrieNode node = root;
        int index = 0;
        try {
            while (!stream.isEmpty()) {
                ++index;
                char symbol = stream.getChar();
                node = goInAhoTree(node, symbol);
                if (node.isTerminating()) {
                    addResult(result, root, node, index);
                }
                AhoTrieNode lastFinal = node;
                while (getFinalLink(lastFinal) != null) {
                    lastFinal = getFinalLink(lastFinal);
                    getSufficsLink(lastFinal);
                    addResult(result, root, lastFinal, index);
                }
            }
        } catch (EmptyStreamException e) {
            throw new RuntimeException(ERROR_STREAM_MESSAGE, e);
        }
        return result;
    }

    private void addResult(List<Occurence> result, AhoTrieNode root, AhoTrieNode node, int index) {
        AhoFinalInfo info = (AhoFinalInfo) node.getTerminatingInfo();
        result.add(new Occurence(((node == root) ? index - 1 : index - info.getTemplate().length()),
                info.getTemplate(), info.getTemplateId()));
    }

    private AhoTrieNode goInAhoTree(AhoTrieNode node, char symbol) {
        AhoTrieNode next = null;
        while (next == null) {
            getSufficsLink(node);
            next = node.getChildAho(symbol);
            if (next == null) {
                if ((node.sufficsLink == node)) {
                    next = node;
                } else {
                    node = node.sufficsLink;
                }
            }
        }
        return next;
    }

    private void getSufficsLink(AhoTrieNode node) {
        if (node.sufficsLink == null) {
            AhoTrieNode parent = node.getParent();
            node.sufficsLink = ((parent.sufficsLink == parent)
                    ? parent : goInAhoTree(parent.sufficsLink, node.getParentEdge()));

        }
    }

    public AhoTrieNode getFinalLink(AhoTrieNode node) {
        if (!node.isSetFinalLink()) {
            getSufficsLink(node);
            AhoTrieNode lnk = node.sufficsLink;
            if (lnk == node) {
                node.finalLink = null;
            } else {
                node.finalLink = (lnk.isTerminating() ? lnk : getFinalLink(lnk));
            }
            node.setFinalLinkWas();
        }
        return node.finalLink;
    }
}

class AhoTrieNode extends TrieNode {
    AhoTrieNode sufficsLink;
    AhoTrieNode finalLink;
    private boolean setFinalLink = false;
    private AhoTrieNode parent;
    private char parentEdge;

    AhoTrieNode(TrieNode parent, char parentEdge) {
        this.parent = (AhoTrieNode) parent;
        this.parentEdge = parentEdge;
    }

    boolean isSetFinalLink() {
        return setFinalLink;
    }

    void setFinalLinkWas() {
        this.setFinalLink = true;
    }

    AhoTrieNode getChildAho(char symbol) {
        return (AhoTrieNode) getChild(symbol);
    }

    AhoTrieNode getParent() {
        return parent;
    }

    char getParentEdge() {
        return parentEdge;
    }
}

class AhoFinalInfo extends TerminatingTrieNodeInfo {
    private int templateId;
    private String template;

    AhoFinalInfo(int templateId, String template) {
        this.templateId = templateId;
        this.template = template;
    }

    int getTemplateId() {
        return templateId;
    }

    String getTemplate() {
        return template;
    }
}
