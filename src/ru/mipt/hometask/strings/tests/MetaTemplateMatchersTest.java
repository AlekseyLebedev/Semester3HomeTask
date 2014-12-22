package ru.mipt.hometask.strings.tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.mipt.hometask.strings.NaiveTemplateMatcher;
import ru.mipt.hometask.strings.StaticTemplateMatcher;
import ru.mipt.hometask.strings.StringStream;
import ru.mipt.hometask.strings.exceptions.TemplateAlreadyExist;
import ru.mipt.hometask.strings.interfaces.IMetaTemplateMatcher;
import ru.mipt.hometask.strings.interfaces.IMetaTemplateMatcherFactory;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class MetaTemplateMatchersTest {
    public static final int MAX_TEST_SIZE = 15;
    private IMetaTemplateMatcherFactory factory;
    private IMetaTemplateMatcher matcher;

    public MetaTemplateMatchersTest(IMetaTemplateMatcherFactory factory) {
        this.factory = factory;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[]{(IMetaTemplateMatcherFactory) (NaiveTemplateMatcher::new)},
                new Object[]{(IMetaTemplateMatcherFactory) (StaticTemplateMatcher::new)}
        );
    }

    @Before
    public void setUp() throws TemplateAlreadyExist {
        matcher = factory.generate();
    }

    @Test
    public void testWorkWithoutTemplates() throws TemplateAlreadyExist {
        Assert.assertEquals(0, matcher.matchStream(new StringStream("")).size());
        Assert.assertEquals(0, matcher.matchStream(new StringStream("qwerty")).size());
        Assert.assertEquals(0, matcher.matchStream(new StringStream("abacabadaba")).size());
        Assert.assertEquals(0, matcher.matchStream(new StringStream("abacaba")).size());
        Assert.assertEquals(0, matcher.matchStream(new StringStream("a")).size());
    }
}