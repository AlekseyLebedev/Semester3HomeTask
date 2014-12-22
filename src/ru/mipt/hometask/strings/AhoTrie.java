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

    //TODO
    public void print(int deph, AhoTrieNode node) throws IOException {
        boolean first = true;
        for (char i = 'a'; i <= 'z' && deph <= 25; i++) {
            if (node.getChildAho(i) != null) {
                if (first) {
                    first = false;
                } else {
                    for (int j = 0; j < deph; j++) {
                        System.out.print(" ");
                    }
                }
                System.out.print(i);
                print(deph + 1, node.getChildAho(i));
            }
        }
        if (first) {
            System.out.println();
        }
    }

    //TODO
    private Set<AhoTrieNode> set=new HashSet<>();
    void dfs(AhoTrieNode node) {
        if(set.contains(node))
            throw new RuntimeException("QWERTYUI");
        set.add(node);
        if (node.sufficsLink == node) {
            if (node != getRootAho()) {
                throw new RuntimeException("All bad!");
            }
        }
        for (char i = 'a'; i <= 'z'; i++) {
            AhoTrieNode childAho = node.getChildAho(i);
            if (childAho != null) {
                dfs(childAho);
            }
        }
    }

    public List<Occurence> searchSubstrings(ICharStream stream) {
        //TODO
        /*try {
            print(0, getRootAho());
        } catch (IOException e) {
            e.printStackTrace();
        }*/
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
                //TODO
                //System.out.print(node.getParentEdge());
                if (node.isTerminating()) {
                    addResult(result, root, node, index);
                }
                AhoTrieNode lastFinal = node;
                getSufficsLink(lastFinal);
                while (lastFinal.getFinalLink() != null) {
                    lastFinal = lastFinal.getFinalLink();
                    addResult(result, root, lastFinal, index);
                    getSufficsLink(lastFinal);
                }
            }
        } catch (EmptyStreamException e) {
            throw new RuntimeException(ERROR_STREAM_MESSAGE, e);
        }
        //TODO
        //dfs(getRootAho());
        return result;
    }

    private void addResult(List<Occurence> result, AhoTrieNode root, AhoTrieNode node, int index) {
        AhoFinalInfo info = (AhoFinalInfo) node.getTerminatingInfo();
        // TODO
        //System.out.println(index + " " + info.getTemplate() + " " + info.getTemplate().length());
        result.add(new Occurence(((node == root) ? index - 1 : index - info.getTemplate().length()),
                info.getTemplate(), info.getTemplateId()));
    }

    private AhoTrieNode goInAhoTree(AhoTrieNode node, char symbol) {
        getSufficsLink(node);
        AhoTrieNode next = node.getChildAho(symbol);
        if (next == null) {
            next = (node.sufficsLink == node) ? node : goInAhoTree(node.sufficsLink, symbol);
        }
        return next;
    }

    private void getSufficsLink(AhoTrieNode node) {
        if (node.sufficsLink == null) {
            AhoTrieNode parent = node.getParent();
            node.sufficsLink = ((parent.sufficsLink == parent) ?
                    parent : goInAhoTree(parent.sufficsLink, node.getParentEdge()));
        }
    }
}

class AhoTrieNode extends TrieNode {
    AhoTrieNode sufficsLink;
    private AhoTrieNode finalLink;
    private boolean setFinalLink = false;
    private AhoTrieNode parent;
    private char parentEdge;

    AhoTrieNode(TrieNode parent, char parentEdge) {
        this.parent = (AhoTrieNode) parent;
        this.parentEdge = parentEdge;
    }

    AhoTrieNode getChildAho(char symbol) {
        return (AhoTrieNode) getChild(symbol);
    }

    public AhoTrieNode getParent() {
        return parent;
    }

    public char getParentEdge() {
        return parentEdge;
    }

    public AhoTrieNode getFinalLink() {
        if (!setFinalLink) {
            AhoTrieNode lnk = this.sufficsLink;
            if (lnk == this) {
                finalLink = null;
            } else {
                finalLink = (lnk.isTerminating() ? lnk : lnk.getFinalLink());
            }
            setFinalLink = true;
        }
        return finalLink;
    }
}

class AhoFinalInfo extends TerminatingTrieNodeInfo {
    private int templateId;
    private String template;

    public AhoFinalInfo(int templateId, String template) {
        this.templateId = templateId;
        this.template = template;
    }

    public int getTemplateId() {
        return templateId;
    }

    public String getTemplate() {
        return template;
    }
}
