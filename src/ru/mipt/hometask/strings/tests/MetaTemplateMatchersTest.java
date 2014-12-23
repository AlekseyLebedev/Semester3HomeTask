package ru.mipt.hometask.strings.tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.mipt.hometask.strings.NaiveTemplateMatcher;
import ru.mipt.hometask.strings.Occurence;
import ru.mipt.hometask.strings.StaticTemplateMatcher;
import ru.mipt.hometask.strings.StringStream;
import ru.mipt.hometask.strings.exceptions.TemplateAlreadyExist;
import ru.mipt.hometask.strings.interfaces.IMetaTemplateMatcher;
import ru.mipt.hometask.strings.interfaces.IMetaTemplateMatcherFactory;

import java.util.*;

@RunWith(Parameterized.class)
public class MetaTemplateMatchersTest {
    public static final int TESTS_COUNT = 4;
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

    public static List<String> getTemplates(byte size, int test) {
        List<String> result = new ArrayList<>();
        Generators generator = Generators.getForSize(size);
        switch (test) {
            case 0:
                for (byte i = 2; i <= size; i++) {
                    switch (i % 6) {
                        case 0:
                            result.add(Generators.getForSize(i).getAllEqualsChars());
                            break;
                        case 1:
                            result.add(Generators.getForSize(i).getAllEqualsCharsInsteadFirst());
                            break;
                        case 2:
                            result.add(Generators.getForSize(i).getAllEqualsCharsInsteadLast());
                            break;
                        case 3:
                            result.add(Generators.getForSize(i).getManyEqualChars());
                            break;
                        case 4:
                            result.add(Generators.getForSize(i).getGrayString());
                            break;
                        case 5:
                            result.add(Generators.getForSize(i).getHalfLengthEqualChars());
                            break;
                    }
                }
                break;
            case 1:
                for (byte i = 2; i <= size; i++) {
                    result.add(Generators.getForSize(i).getGrayString());
                }
                break;
            case 2:
                for (byte i = 4; i < size; i++) {
                    add(result, generator.getAllEqualsChars());
                    add(result, generator.getAllEqualsCharsInsteadFirst());
                    add(result, generator.getAllEqualsCharsInsteadLast());
                    add(result, generator.getGrayString());
                    add(result, generator.getHalfLengthEqualChars());
                    add(result, generator.getManyEqualChars());
                }
                break;
            case 3:
                for (byte i = 2; i <= size; i++) {
                    result.add(Generators.getForSize(i).getAllEqualsChars());
                }
                break;
        }
        return result;
    }

    public static List<String> getStreams(byte size, int test) {
        List<String> result = new ArrayList<>();
        Generators generator = Generators.getForSize(size);
        switch (test) {
            case 0:
                result.add(generator.getAllEqualsChars());
                result.add(generator.getAllEqualsCharsInsteadFirst());
                result.add(generator.getAllEqualsCharsInsteadLast());
                result.add(generator.getEmptyString());
                result.add(generator.getGrayString());
                result.add(generator.getHalfLengthEqualChars());
                result.add(generator.getManyEqualChars());
                break;
            case 1:
                for (byte i = 2; i <= size; i++) {
                    result.add(Generators.getForSize(i).getGrayString());
                }
                break;
            case 2:
                for (byte i = 4; i < size; i++) {
                    add(result, generator.getAllEqualsChars());
                    add(result, generator.getAllEqualsCharsInsteadFirst());
                    add(result, generator.getAllEqualsCharsInsteadLast());
                    add(result, generator.getGrayString());
                    add(result, generator.getHalfLengthEqualChars());
                    add(result, generator.getManyEqualChars());
                }
                break;
            case 3:
                for (byte i = 2; i <= size; i++) {
                    result.add(Generators.getForSize(i).getAllEqualsChars());
                }
                break;
        }
        return result;
    }

    private static void add(List<String> result, String text) {
        if (!result.contains(text)) {
            result.add(text);
        }
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

    public long checkStream(List<String> templates, int maxTemplate, String value, IMetaTemplateMatcher matcher) {
        return Utils.matchStreamAndCountMilliseconds(matcher, value, occurences -> {
            int count = 0;
            Collections.sort(occurences);
            for (int i = 0; i < value.length(); i++) {
                for (int j = 0; j < maxTemplate; j++) {
                    String template = templates.get(j);
                    if (checkSubstring(value, i, template)) {
                        Occurence occurence = occurences.get(count);
                        Assert.assertEquals(i, occurence.getPosition());
                        Assert.assertEquals(j, occurence.getTemplateId());
                        Assert.assertEquals(template, occurence.getTemplate());
                        ++count;
                    }
                }
            }
            Assert.assertEquals(count, occurences.size());
        });
    }

    private boolean checkSubstring(String value, int index, String template) {
        for (int i = 0; i < template.length(); i++) {
            if (index + i >= value.length()) {
                return false;
            }
            if (template.charAt(i) != value.charAt(i + index)) {
                return false;
            }
        }
        return true;
    }

    public void runTest(List<String> templates, List<String> streams) throws TemplateAlreadyExist {
        IMetaTemplateMatcher matcher = factory.generate();
        for (int i = 0; i < templates.size(); i++) {
            Assert.assertEquals(i, matcher.addTemplate(templates.get(i)));
            if (canAddTemplateAfterStream) {
                for (String stream : streams) {
                    checkStream(templates, i + 1, stream, matcher);
                }
            }
        }
        for (String stream : streams) {
            checkStream(templates, templates.size(), stream, matcher);
        }
    }

    @Test
    public void testCorrect() throws TemplateAlreadyExist {
        for (int i = 0; i < TESTS_COUNT; i++) {
            for (byte size = 3; size < 14; size++) {
                if (!(i == 2 && size == 14)) {
                    runTest(getTemplates(size, i), getStreams(size, i));
                }
            }
        }
    }
}
