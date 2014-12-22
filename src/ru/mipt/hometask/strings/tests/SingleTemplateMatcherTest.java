package ru.mipt.hometask.strings.tests;

import org.junit.Assert;
import org.junit.Test;
import ru.mipt.hometask.strings.Occurence;
import ru.mipt.hometask.strings.SingleTemplateMatcher;
import ru.mipt.hometask.strings.StringStream;

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