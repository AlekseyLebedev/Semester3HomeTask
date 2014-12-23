package ru.mipt.hometask.strings;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class TrieNode {
    private TerminatingTrieNodeInfo termination;
    private Map<Character, TrieNode> children = new TreeMap<>();

    public final boolean isTerminating() {
        return termination != null;
    }

    public void setTerminating(TerminatingTrieNodeInfo info) {
        termination = info;
    }

    public final TrieNode getChild(char symbol) {
        return children.get(symbol);
    }

    public final void addChild(char symbol, TrieNode node) {
        TrieNode oldNode = children.put(symbol, node);
        if (oldNode != null) {
            children.put(symbol, oldNode);
            throw new IllegalStateException("The edge to node with symbol '" + symbol + "' has been already added");
        }
    }

    public final TerminatingTrieNodeInfo getTerminatingInfo() {
        if (isTerminating()) {
            return termination;
        } else {
            throw new IllegalStateException("This node is not terminal");
        }
    }

    public void merge(TrieNode other) {
        other.children.forEach((Character character, TrieNode trieNode) -> {
            TrieNode child = getChild(character);
            if (child == null) {
                addChild(character, trieNode);
            } else {
                child.merge(trieNode);
            }
        });
        other.children.clear();
        if (other.isTerminating() && this.isTerminating()) {
            throw new IllegalStateException("Unmergable tries");
        }
        if (other.isTerminating()) {
            this.termination = other.getTerminatingInfo();
            other.termination = null;
        }
    }

    public final Map<Character, TrieNode> getAllChild() {
        return Collections.unmodifiableMap(children);
    }
}
