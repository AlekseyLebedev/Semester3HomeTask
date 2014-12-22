package ru.mipt.hometask.strings.tests;

import org.junit.Assert;
import org.junit.Test;
import ru.mipt.hometask.strings.StringStream;
import ru.mipt.hometask.strings.Trie;
import ru.mipt.hometask.strings.exceptions.EmptyStreamException;
import ru.mipt.hometask.strings.interfaces.ICharStream;

import java.util.Arrays;
import java.util.List;

interface Lambda {
    public boolean add(Trie trie, String string);
}

public class TrieTest {
    @Test
    public void testAddAndSearch() throws Exception {
        testAddAndSearch((Trie trie, String input) -> trie.addString(input));
        testAddAndSearch((Trie trie, String input) -> trie.addStream(new StringStream(input)));
    }

    private void testAddAndSearch(Lambda addMethod) {
        Trie trie = new Trie();
        List<String> list = Arrays.asList(
                "qwerty",
                "test",
                "Qwerty",
                "TestString",
                "Text",
                "Message",
                "cdc",
                "cdcecdc",
                "cd",
                "c",
                "cdf",
                ""
        );
        String[] grayStrings = new String[10];
        for (byte i = 0; i < 10; i++) {
            grayStrings[i] = Utils.getInstance().grayString(i);
        }
        for (String test : list) {
            assertNotFound(trie, test);
        }
        assertThereIsNoStrings(trie, grayStrings);
        Assert.assertTrue(addMethod.add(trie, "qwerty"));
        assertFound(trie, "qwerty");
        assertNotFound(trie, "Qwerty");
        assertNotFound(trie, "qwErty");
        assertThereIsNoStrings(trie, grayStrings);
        Assert.assertFalse(addMethod.add(trie, "qwerty"));
        for (int i = 1; i < list.size(); ++i) {
            assertThereIsNoStrings(trie, grayStrings);
            for (int j = 0; j < i; j++) {
                assertFound(trie, list.get(j));
            }
            for (int j = i; j < list.size(); j++) {
                assertNotFound(trie, list.get(j));
            }
            Assert.assertTrue(addMethod.add(trie, list.get(i)));
            for (int j = 0; j <= i; j++) {
                assertFound(trie, list.get(j));
            }
            for (int j = i + 1; j < list.size(); j++) {
                assertNotFound(trie, list.get(j));
            }
            assertThereIsNoStrings(trie, grayStrings);
        }
        for (int i = 0; i < 5; i++) {
            assertThereIsList(trie, list);
            for (int j = 0; j < i; j++) {
                assertFound(trie, list.get(j));
            }
            for (int j = i; j < grayStrings.length; j++) {
                assertNotFound(trie, grayStrings[j]);
            }
            Assert.assertTrue(addMethod.add(trie, grayStrings[i]));
            for (int j = 0; j <= i; j++) {
                assertFound(trie, grayStrings[j]);
            }
            for (int j = i + 1; j < grayStrings.length; j++) {
                assertNotFound(trie, grayStrings[j]);
            }
            assertThereIsList(trie, list);
            for (int j = 0; j < i; j += 2) {
                Assert.assertFalse(addMethod.add(trie, grayStrings[i]));
            }
        }
        for (int i = 9; i >= 5; --i) {
            assertThereIsList(trie, list);
            for (int j = 0; j < 5; j++) {
                assertFound(trie, grayStrings[j]);
            }
            for (int j = i + 1; j < grayStrings.length; j++) {
                assertFound(trie, grayStrings[j]);
            }
            for (int j = 5; j <= i; j++) {
                assertNotFound(trie, grayStrings[j]);
            }
            Assert.assertTrue(addMethod.add(trie, grayStrings[i]));
            for (int j = 0; j < 5; j++) {
                assertFound(trie, grayStrings[j]);
            }
            for (int j = i; j < grayStrings.length; j++) {
                assertFound(trie, grayStrings[j]);
            }
            for (int j = 5; j < i; j++) {
                assertNotFound(trie, grayStrings[j]);
            }
            assertThereIsList(trie, list);
            for (int j = 0; j < i; j += 2) {
                Assert.assertFalse(addMethod.add(trie, grayStrings[i]));
            }
        }
        for (int j = 0; j < grayStrings.length; j += 2) {
            Assert.assertFalse(addMethod.add(trie, grayStrings[j]));
        }
    }

    private void assertThereIsNoStrings(Trie trie, String[] strings) {
        for (String test : strings) {
            assertNotFound(trie, test);
        }
    }

    private void assertThereIsList(Trie trie, List<String> strings) {
        for (String test : strings) {
            assertFound(trie, test);
        }
    }

    private void assertNotFound(Trie trie, String test) {
        Assert.assertNull(trie.search(test));
        Assert.assertNull(trie.search(new StringStream(test)));
    }


    private void assertFound(Trie trie, String test) {
        Assert.assertNotNull(trie.search(test));
        Assert.assertNotNull(trie.search(new StringStream(test)));
    }

    @Test
    public void testGetRoot() throws Exception {
        Trie trie = new Trie();
        trie.addString("abacabad");
        trie.addString("d");
        trie.addString("ewq");
        trie.addString("qwerty");
        for (char c = 'a'; c <= 'z'; ++c) {
            switch (c) {
                case 'a':
                case 'd':
                case 'e':
                case 'q':
                    Assert.assertNotNull(trie.getRoot().getChild(c));
                    break;
                default:
                    Assert.assertNull(trie.getRoot().getChild(c));
            }
        }
    }

    @Test(expected = RuntimeException.class)
    public void testSearchThrowsExceptionOnWrongStream() {
        Trie trie = new Trie();
        trie.addStream(new BadStream());
    }

    @Test(expected = RuntimeException.class)
    public void testAddThrowsExceptionOnWrongStream() {
        Trie trie = new Trie();
        trie.search(new BadStream());
    }
}

class BadStream implements ICharStream {

    @Override
    public char getChar() throws EmptyStreamException {
        throw new EmptyStreamException();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
