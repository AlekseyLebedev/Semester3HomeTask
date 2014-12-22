package ru.mipt.hometask.strings.tests;

import org.junit.Assert;
import org.junit.Test;
import ru.mipt.hometask.strings.NaiveTemplateMatcher;
import ru.mipt.hometask.strings.StringStream;

public class NaiveTemplateMatcherTest {
    @Test
    public void testWorkWithoutTemplates() {
        NaiveTemplateMatcher matcher = new NaiveTemplateMatcher();
        Assert.assertEquals(0, matcher.matchStream(new StringStream("")).size());
        Assert.assertEquals(0, matcher.matchStream(new StringStream("qwerty")).size());
        Assert.assertEquals(0, matcher.matchStream(new StringStream("abacabadaba")).size());
        Assert.assertEquals(0, matcher.matchStream(new StringStream("abacaba")).size());
        Assert.assertEquals(0, matcher.matchStream(new StringStream("a")).size());
    }
}