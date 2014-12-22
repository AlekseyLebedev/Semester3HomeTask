package ru.mipt.hometask.strings.tests;

import org.junit.Test;
import ru.mipt.hometask.strings.StaticTemplateMatcher;

public class StaticTemplateMatcherTest {
    @Test(expected = IllegalStateException.class)
    public void testThrowsExceptionWhenAddTemplateAfterMatchingStream0() throws Exception {
        StaticTemplateMatcher matcher = new StaticTemplateMatcher();
        matcher.matchStream(new EmptyStream());
        matcher.addTemplate("qwTest");
    }

    @Test(expected = IllegalStateException.class)
    public void testThrowsExceptionWhenAddTemplateAfterMatchingStream1() throws Exception {
        StaticTemplateMatcher matcher = new StaticTemplateMatcher();
        matcher.addTemplate("qwerty");
        matcher.matchStream(new EmptyStream());
        matcher.addTemplate("qwTest");
    }

    @Test(expected = IllegalStateException.class)
    public void testThrowsExceptionWhenAddTemplateAfterMatchingStream2() throws Exception {
        StaticTemplateMatcher matcher = new StaticTemplateMatcher();
        matcher.addTemplate("qwerty");
        matcher.addTemplate("abacaba");
        matcher.matchStream(new EmptyStream());
        matcher.addTemplate("qwTest");
    }
}
