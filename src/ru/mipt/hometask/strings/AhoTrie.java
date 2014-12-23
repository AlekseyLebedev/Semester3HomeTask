package ru.mipt.hometask.strings;

import ru.mipt.hometask.strings.exceptions.EmptyStreamException;
import ru.mipt.hometask.strings.interfaces.ICharStream;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AhoTrie extends Trie {
    private int templateId;
    private String template;

    public static List<Occurence> searchSubstrings(ICharStream stream, List<AhoTrie> tries) {
        List<Occurence> result = new ArrayList<>();
        List<AhoTrie> actualTries = tries.stream().filter(item -> item != null).collect(Collectors.toList());
        AhoTrieNode[] nodes = new AhoTrieNode[actualTries.size()];
        for (int i = 0; i < nodes.length; i++) {
            AhoTrieNode root = actualTries.get(i).getRootAho();
            root.isValid();
            root.sufficsLink = root;
            nodes[i] = root;
        }
        int index = 0;
        try {
            while (!stream.isEmpty()) {
                ++index;
                char symbol = stream.getChar();
                for (int i = 0; i < nodes.length; i++) {
                    AhoTrie trie = actualTries.get(i);
                    nodes[i] = trie.goInAhoTree(nodes[i], symbol);
                    AhoTrieNode root = trie.getRootAho();
                    if (nodes[i].isTerminating()) {
                        trie.addResult(result, root, nodes[i], index);
                    }
                    AhoTrieNode lastFinal = nodes[i];
                    while (trie.getFinalLink(lastFinal) != null) {
                        lastFinal = trie.getFinalLink(lastFinal);
                        trie.getSufficsLink(lastFinal);
                        trie.addResult(result, root, lastFinal, index);
                    }
                }
            }
        } catch (EmptyStreamException e) {
            throw new RuntimeException(ERROR_STREAM_MESSAGE, e);
        }
        return result;
    }


    @Override
    protected TrieNode generateNode(char edge, TrieNode parent) {
        return new AhoTrieNode(parent, edge);
    }

    @Override
    protected TerminatingTrieNodeInfo generateTerminatingInfo() {
        return new AhoFinalInfo(templateId, template);
    }

    @Override
    public void merge(Trie other) {
        super.merge(other);
        getRootAho().invalidate();
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
        root.isValid();
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
        node.isValid();
        if (node.sufficsLink == null) {
            AhoTrieNode parent = node.getParent();
            node.sufficsLink = ((parent.sufficsLink == parent)
                    ? parent : goInAhoTree(parent.sufficsLink, node.getParentEdge()));

        }
    }

    public AhoTrieNode getFinalLink(AhoTrieNode node) {
        node.isValid();
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
    private boolean valid;

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

    public void setParent(AhoTrieNode parent) {
        this.parent = parent;
    }

    char getParentEdge() {
        return parentEdge;
    }

    void invalidate() {
        getAllChild().forEach((Character character, TrieNode trieNode) -> {
            AhoTrieNode node = (AhoTrieNode) trieNode;
            node.valid = false;
        });
    }

    @Override
    public List<Pair<TrieNode, TrieNode>> merge(TrieNode other) {
        List<Pair<TrieNode, TrieNode>> merge = super.merge(other);
        getAllChild().forEach((Character character, TrieNode trieNode) -> {
            AhoTrieNode node = (AhoTrieNode) trieNode;
            node.setParent(this);
            node.valid = false;
        });
        return merge;
    }

    boolean isValid() {
        if (!valid) {
            finalLink = null;
            sufficsLink = null;
            setFinalLink = false;
            invalidate();
            valid = true;
        }
        return valid;
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
