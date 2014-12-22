package ru.mipt.hometask.strings.tests;

import org.junit.Assert;
import org.junit.Test;
import ru.mipt.hometask.strings.Occurence;
import ru.mipt.hometask.strings.SingleTemplateMatcher;
import ru.mipt.hometask.strings.StringStream;
import ru.mipt.hometask.strings.exceptions.EmptyStreamException;
import ru.mipt.hometask.strings.exceptions.TemplateAlreadyExist;
import ru.mipt.hometask.strings.interfaces.ICharStream;
import ru.mipt.hometask.strings.interfaces.IMetaTemplateMatcher;

import java.util.Collections;
import java.util.List;

public class SingleTemplateMatcherTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testAddTemplateThrowsExceptionThanAdd2Templates() throws Exception {
        SingleTemplateMatcher matcher = new SingleTemplateMatcher();
        matcher.addTemplate("qwerty");
        matcher.addTemplate("abcd");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddTemplateThrowsExceptionThanAdd2Templates2() throws Exception {
        SingleTemplateMatcher matcher = new SingleTemplateMatcher();
        matcher.addTemplate("qwerty");
        matcher.addTemplate("qwerty");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMatchStreamThrowsExceptionThanNoTemplate() throws Exception {
        SingleTemplateMatcher matcher = new SingleTemplateMatcher();
        matcher.matchStream(new EmptyStream());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAppendCharToTemplateThrowsExceptionThanNoTemplate() throws Exception {
        SingleTemplateMatcher matcher = new SingleTemplateMatcher();
        matcher.appendCharToTemplate('a');
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPrependCharToTemplateThrowsExceptionThanNoTemplate() throws Exception {
        SingleTemplateMatcher matcher = new SingleTemplateMatcher();
        matcher.prependCharToTemplate('z');
    }

    @Test
    public void testAppendAndPrependCharToTemplate() throws Exception {
        SingleTemplateMatcher matcher = new SingleTemplateMatcher();
        matcher.addTemplate("qw");
        List<Occurence> result = matcher.matchStream(new StringStream("baqwertybaqweqwcaqw"));
        Assert.assertEquals(4, result.size());
        Collections.sort(result);
        Assert.assertEquals(2, result.get(0).getPosition());
        Assert.assertEquals(10, result.get(1).getPosition());
        Assert.assertEquals(13, result.get(2).getPosition());
        Assert.assertEquals(17, result.get(3).getPosition());
        matcher.prependCharToTemplate('a');
        result = matcher.matchStream(new StringStream("baqwertybaqweqwcaqw"));
        Assert.assertEquals(3, result.size());
        Collections.sort(result);
        Assert.assertEquals(1, result.get(0).getPosition());
        Assert.assertEquals(9, result.get(1).getPosition());
        Assert.assertEquals(16, result.get(2).getPosition());
        matcher.appendCharToTemplate('e');
        result = matcher.matchStream(new StringStream("baqwertybaqweqwcaqw"));
        Assert.assertEquals(2, result.size());
        Collections.sort(result);
        Assert.assertEquals(1, result.get(0).getPosition());
        Assert.assertEquals(9, result.get(1).getPosition());
        matcher.appendCharToTemplate('r');
        result = matcher.matchStream(new StringStream("baqwertybaqweqwcaqw"));
        Assert.assertEquals(1, result.size());
        Collections.sort(result);
        Assert.assertEquals(1, result.get(0).getPosition());
        matcher.appendCharToTemplate('r');
        result = matcher.matchStream(new StringStream("baqwertybaqweqwcaqw"));
        Assert.assertEquals(0, result.size());
        matcher = new SingleTemplateMatcher();
        matcher.addTemplate("qw");
        result = matcher.matchStream(new StringStream("baqwertybaqweqwcaqw"));
        Assert.assertEquals(4, result.size());
        Collections.sort(result);
        Assert.assertEquals(2, result.get(0).getPosition());
        Assert.assertEquals(10, result.get(1).getPosition());
        Assert.assertEquals(13, result.get(2).getPosition());
        Assert.assertEquals(17, result.get(3).getPosition());
        matcher.prependCharToTemplate('a');
        result = matcher.matchStream(new StringStream("baqwertybaqweqwcaqw"));
        Assert.assertEquals(3, result.size());
        Collections.sort(result);
        Assert.assertEquals(1, result.get(0).getPosition());
        Assert.assertEquals(9, result.get(1).getPosition());
        Assert.assertEquals(16, result.get(2).getPosition());
        matcher.prependCharToTemplate('b');
        result = matcher.matchStream(new StringStream("baqwertybaqweqwcaqw"));
        Assert.assertEquals(2, result.size());
        Collections.sort(result);
        Assert.assertEquals(0, result.get(0).getPosition());
        Assert.assertEquals(8, result.get(1).getPosition());
        matcher.prependCharToTemplate('y');
        result = matcher.matchStream(new StringStream("baqwertybaqweqwcaqw"));
        Assert.assertEquals(1, result.size());
        Collections.sort(result);
        Assert.assertEquals(7, result.get(0).getPosition());
        matcher.appendCharToTemplate('r');
        result = matcher.matchStream(new StringStream("baqwertybaqweqwcaqw"));
        Assert.assertEquals(0, result.size());
        matcher = new SingleTemplateMatcher();
        matcher.addTemplate("qw");
        result = matcher.matchStream(new StringStream("baqwertybaqweqwcaqw"));
        Assert.assertEquals(4, result.size());
        Collections.sort(result);
        Assert.assertEquals(2, result.get(0).getPosition());
        Assert.assertEquals(10, result.get(1).getPosition());
        Assert.assertEquals(13, result.get(2).getPosition());
        Assert.assertEquals(17, result.get(3).getPosition());
        matcher.appendCharToTemplate('c');
        result = matcher.matchStream(new StringStream("baqwertybaqweqwcaqw"));
        Assert.assertEquals(1, result.size());
        Collections.sort(result);
        Assert.assertEquals(13, result.get(0).getPosition());
    }
}

class SingleTemplateWrapper implements IMetaTemplateMatcher {
    SingleTemplateMatcher firstHalf;
    SingleTemplateMatcher secondHalf;
    SingleTemplateMatcher middle;

    @Override
    public int addTemplate(String template) throws TemplateAlreadyExist {
        SingleTemplateMatchersTests.expectedTemplateId = 0;
        firstHalf = new SingleTemplateMatcher();
        secondHalf = new SingleTemplateMatcher();
        middle = new SingleTemplateMatcher();
        int length = template.length();
        int halfLength = length / 2;
        int quoter = length / 4;
        Assert.assertEquals(0, firstHalf.addTemplate(template.substring(0, halfLength)));
        Assert.assertEquals(0, secondHalf.addTemplate(template.substring(halfLength, length)));
        Assert.assertEquals(0, middle.addTemplate(template.substring(quoter, quoter + halfLength)));
        for (int i = halfLength; i < length; i++) {
            firstHalf.appendCharToTemplate(template.charAt(i));
        }
        for (int i = halfLength; i > 0; i--) {
            secondHalf.prependCharToTemplate(template.charAt(i - 1));
        }
        for (int i = quoter; i > 0; i--) {
            middle.prependCharToTemplate(template.charAt(i - 1));
        }
        for (int i = quoter + halfLength; i < length; i++) {
            middle.appendCharToTemplate(template.charAt(i));
        }
        return 0;
    }

    @Override
    public List<Occurence> matchStream(ICharStream stream) {
        StringBuilder builder = new StringBuilder();
        try {
            while (!stream.isEmpty()) {
                builder.append(stream.getChar());
            }
        } catch (EmptyStreamException e) {
            firstHalf.matchStream(stream);
        }
        String input = builder.toString();
        List<Occurence> list1 = firstHalf.matchStream(new StringStream(input));
        List<Occurence> list2 = secondHalf.matchStream(new StringStream(input));
        List<Occurence> list3 = middle.matchStream(new StringStream(input));
        Assert.assertEquals(list1.size(), list2.size());
        Assert.assertEquals(list1.size(), list3.size());
        Collections.sort(list1);
        Collections.sort(list2);
        Collections.sort(list3);
        for (int i = 0; i < list1.size(); i++) {
            Assert.assertEquals(list1.get(i).getPosition(), list2.get(i).getPosition());
            Assert.assertEquals(list1.get(i).getPosition(), list3.get(i).getPosition());
        }
        return list1;
    }
}
