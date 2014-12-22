package ru.mipt.hometask.strings;

import ru.mipt.hometask.strings.exceptions.EmptyStreamException;
import ru.mipt.hometask.strings.interfaces.ICharStream;

public class Trie {
    protected static final String ERROR_STREAM_MESSAGE = "There is an error in input stream";
    private TrieNode root;

    public Trie() {
        root = generateNode('q', null);
    }

    public final TerminatingTrieNodeInfo search(String mask) {
        return search(new StringStream(mask));
    }

    public final TerminatingTrieNodeInfo search(ICharStream stream) {
        TrieNode node = root;
        try {
            while (!stream.isEmpty()) {
                node = node.getChild(stream.getChar());
                if (node == null) {
                    return null;
                }
            }
        } catch (EmptyStreamException e) {
            throw new RuntimeException(ERROR_STREAM_MESSAGE, e);
        }
        if (node.isTerminating()) {
            return node.getTerminatingInfo();
        } else {
            return null;
        }
    }

    public boolean addString(String input) {
        return addStream(new StringStream(input));
    }


    public boolean addStream(ICharStream stream) {
        TrieNode node = root;
        try {
            while (!stream.isEmpty()) {
                TrieNode parent = node;
                char symbol = stream.getChar();
                node = parent.getChild(symbol);
                if (node == null) {
                    node = generateNode(symbol, parent);
                    parent.addChild(symbol, node);
                }
            }
        } catch (EmptyStreamException e) {
            throw new RuntimeException(ERROR_STREAM_MESSAGE, e);
        }
        if (node.isTerminating()) {
            return false;
        } else {
            node.setTerminating(generateTerminatingInfo());
            return true;
        }
    }

    protected TrieNode generateNode(char edge, TrieNode parent) {
        return new TrieNode();
    }

    protected TerminatingTrieNodeInfo generateTerminatingInfo() {
        return new TerminatingTrieNodeInfo();
    }

    public final TrieNode getRoot() {
        return root;
    }
}
