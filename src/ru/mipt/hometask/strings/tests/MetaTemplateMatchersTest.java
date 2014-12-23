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
import java.util.List;

@RunWith(Parameterized.class)
public class MetaTemplateMatchersTest {
    public static final int MAX_TEST_SIZE = 15;
    private IMetaTemplateMatcherFactory factory;
    private IMetaTemplateMatcher matcher;
    private boolean canAddTemplateAfterStream;

    public MetaTemplateMatchersTest(IMetaTemplateMatcherFactory factory, boolean canAddTemplateAfterStream) {
        this.factory = factory;
        this.canAddTemplateAfterStream = canAddTemplateAfterStream;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[]{(IMetaTemplateMatcherFactory) (NaiveTemplateMatcher::new), true},
                new Object[]{(IMetaTemplateMatcherFactory) (StaticTemplateMatcher::new), false}
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

    @Test
    public void testAddAndSearch() throws TemplateAlreadyExist {
        IMetaTemplateMatcher matcher = this.matcher;
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
            assertNotFound(matcher, test);
        }
        assertThereIsNoStrings(matcher, grayStrings);
        matcher.addTemplate("qwerty");
        assertFound(matcher, "qwerty");
        assertNotFound(matcher, "Qwerty");
        assertNotFound(matcher, "qwErty");
        assertThereIsNoStrings(matcher, grayStrings);
        try {
            matcher.addTemplate("qwerty");
            Assert.fail("Should throw exception");
        } catch (TemplateAlreadyExist e) {
            // It should throw
        }
        for (int i = 1; i < list.size(); ++i) {
            for (int j = 0; j < i; j++) {
                assertFound(matcher, list.get(j));
            }
            matcher.addTemplate(list.get(i));
            for (int j = 0; j <= i && j < list.size() - 1; j++) {
                assertFound(matcher, list.get(j));
            }
            for (int j = i + 1; j < grayStrings.length; j++) {
                assertNotFound(matcher, grayStrings[j]);
            }
        }
        for (int i = 0; i < 5; i++) {
            assertThereIsList(matcher, list);
            for (int j = 0; j < i; j++) {
                assertFound(matcher, list.get(j));
            }
            matcher.addTemplate(grayStrings[i]);
            for (int j = 0; j <= i; j++) {
                assertFound(matcher, grayStrings[j]);
            }
            assertThereIsList(matcher, list);
            for (int j = 0; j < i; j += 2) {
                try {
                    matcher.addTemplate(grayStrings[j]);
                    Assert.fail("Should throw exception");
                } catch (TemplateAlreadyExist e) {
                    // It should throw
                }
            }
        }
        for (int i = 9; i >= 5; --i) {
            assertThereIsList(matcher, list);
            matcher.addTemplate(grayStrings[i]);
            for (int j = 0; j < grayStrings.length; j++) {
                assertFound(matcher, grayStrings[j]);
            }
            assertThereIsList(matcher, list);
            for (int j = 0; j < 5; j += 2) {
                try {
                    matcher.addTemplate(grayStrings[j]);
                    Assert.fail("Should throw exception");
                } catch (TemplateAlreadyExist e) {
                    // It should throw
                }
            }
        }
        for (int j = 0; j < grayStrings.length; j += 2) {
            try {
                matcher.addTemplate(grayStrings[j]);
                Assert.fail("Should throw exception");
            } catch (TemplateAlreadyExist e) {
                // It should throw
            }
        }
        for (String test : grayStrings) {
            Assert.assertFalse(matcher.matchStream(new StringStream(test)).isEmpty());
        }
        for (String test : list) {
            if (!test.equals("")) {
                Assert.assertFalse(matcher.matchStream(new StringStream(test)).isEmpty());
            }
        }
    }

    private void assertThereIsNoStrings(IMetaTemplateMatcher matcher, String[] strings) {
        for (String test : strings) {
            assertNotFound(matcher, test);
        }
    }

    private void assertThereIsList(IMetaTemplateMatcher matcher, List<String> strings) {
        for (String test : strings) {
            if (!test.equals("")) {
                assertFound(matcher, test);
            }
        }
    }

    private void assertFound(IMetaTemplateMatcher matcher, String test) {
        if (canAddTemplateAfterStream) {
            Assert.assertTrue(matcher.matchStream(new StringStream(test)).size() > 0);
        }
    }

    private void assertNotFound(IMetaTemplateMatcher matcher, String test) {
        if (canAddTemplateAfterStream) {
            Assert.assertEquals(0, matcher.matchStream(new StringStream(test)).size());
        }
    }
}
