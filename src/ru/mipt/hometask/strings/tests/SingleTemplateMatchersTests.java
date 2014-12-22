package ru.mipt.hometask.strings.tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.mipt.hometask.strings.*;
import ru.mipt.hometask.strings.exceptions.TemplateAlreadyExist;
import ru.mipt.hometask.strings.interfaces.IMetaTemplateMatcher;
import ru.mipt.hometask.strings.interfaces.IMetaTemplateMatcherFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@RunWith(Parameterized.class)
public class SingleTemplateMatchersTests {
    public static final int MAX_TEST_SIZE = 15;
    static int expectedTemplateId = 0;
    private IMetaTemplateMatcherFactory factory;
    private IMetaTemplateMatcher matcher;

    public SingleTemplateMatchersTests(IMetaTemplateMatcherFactory factory) {
        this.factory = factory;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[]{(IMetaTemplateMatcherFactory) (NaiveTemplateMatcher::new)},
                new Object[]{(IMetaTemplateMatcherFactory) (SingleTemplateMatcher::new)},
                new Object[]{(IMetaTemplateMatcherFactory) (StaticTemplateMatcher::new)},
                new Object[]{(IMetaTemplateMatcherFactory) (() -> {
                    NaiveTemplateMatcher result = new NaiveTemplateMatcher();
                    Assert.assertEquals(0, result.addTemplate("****"));
                    expectedTemplateId = 1;
                    return result;
                })},
                new Object[]{(IMetaTemplateMatcherFactory) (() -> {
                    expectedTemplateId = 1;
                    StaticTemplateMatcher value = new StaticTemplateMatcher();
                    Assert.assertEquals(0, value.addTemplate("****"));
                    return value;
                })}
        );
    }

    public static long testOnSameStringsOfEqualChars(Generators generator, IMetaTemplateMatcher matcher)
            throws TemplateAlreadyExist {
        return testOnSameStringsOfEqualChars(generator, matcher, occurrences -> {
            Assert.assertEquals(1, occurrences.size());
            Assert.assertEquals(0, occurrences.get(0).getPosition());
            Assert.assertEquals(expectedTemplateId, occurrences.get(0).getTemplateId());
        });
    }

    public static long testOnSameStringsOfEqualChars(Generators generator, IMetaTemplateMatcher matcher,
                                                     Consumer<List<Occurence>> check) throws TemplateAlreadyExist {
        Assert.assertEquals(expectedTemplateId, matcher.addTemplate(generator.getAllEqualsChars()));
        return Utils.matchStreamAndCountMilliseconds(matcher, generator.getAllEqualsChars(),
                check);
    }

    public static long testOnEmptyTemplatesWithStringOfEqualChars(Generators generator, IMetaTemplateMatcher matcher)
            throws TemplateAlreadyExist {
        return testOnEmptyTemplatesWithStringOfEqualChars(generator, matcher, occurences -> {
            Assert.assertEquals(generator.getSize(), occurences.size());
            Collections.sort(occurences);
            for (int i = 0; i < occurences.size(); i++) {
                Assert.assertEquals(i, occurences.get(i).getPosition());
                Assert.assertEquals(expectedTemplateId, occurences.get(i).getTemplateId());
            }
        });
    }

    public static long testOnEmptyTemplatesWithStringOfEqualChars(Generators generator, IMetaTemplateMatcher matcher,
                                                                  Consumer<List<Occurence>> check)
            throws TemplateAlreadyExist {
        Assert.assertEquals(expectedTemplateId, matcher.addTemplate(generator.getEmptyString()));
        return Utils.matchStreamAndCountMilliseconds(matcher, generator.getAllEqualsChars(), check);
    }


    public static long testOnOneCharTemplatesWithStringOfEqualChars(Generators generator, IMetaTemplateMatcher matcher)
            throws TemplateAlreadyExist {
        return testOnOneCharTemplatesWithStringOfEqualChars(generator, matcher, occurences -> {
            Assert.assertEquals(generator.getSize(), occurences.size());
            Collections.sort(occurences);
            for (int i = 0; i < occurences.size(); i++) {
                Assert.assertEquals(i, occurences.get(i).getPosition());
                Assert.assertEquals(expectedTemplateId, occurences.get(i).getTemplateId());
            }
        });
    }

    public static long testOnOneCharTemplatesWithStringOfEqualChars(Generators generator, IMetaTemplateMatcher matcher,
                                                                    Consumer<List<Occurence>> check)
            throws TemplateAlreadyExist {
        matcher.addTemplate(generator.getOneCharString());
        return Utils.matchStreamAndCountMilliseconds(matcher, generator.getAllEqualsChars(),
                check);
    }

    public static long testOnStringDifferentInLastChar(Generators generator, IMetaTemplateMatcher matcher)
            throws TemplateAlreadyExist {
        return testOnStringDifferentInLastChar(generator, matcher, occurences ->
                Assert.assertEquals(0, occurences.size()));
    }

    public static long testOnStringDifferentInLastChar(Generators generator, IMetaTemplateMatcher matcher,
                                                       Consumer<List<Occurence>> check)
            throws TemplateAlreadyExist {
        matcher.addTemplate(generator.getAllEqualsChars());
        return Utils.matchStreamAndCountMilliseconds(matcher, generator.getAllEqualsCharsInsteadLast(),
                check);
    }

    public static long testOnStringDifferentInFirstChar(Generators generator, IMetaTemplateMatcher matcher)
            throws TemplateAlreadyExist {
        return testOnStringDifferentInFirstChar(generator, matcher, occurences ->
                Assert.assertEquals(0, occurences.size()));
    }

    public static long testOnStringDifferentInFirstChar(Generators generator, IMetaTemplateMatcher matcher,
                                                        Consumer<List<Occurence>> check)
            throws TemplateAlreadyExist {
        matcher.addTemplate(generator.getAllEqualsChars());
        return Utils.matchStreamAndCountMilliseconds(matcher, generator.getAllEqualsCharsInsteadFirst(),
                check);
    }

    public static long testOnEqualGrayStrings(Generators generator, IMetaTemplateMatcher matcher)
            throws TemplateAlreadyExist {
        return testOnEqualGrayStrings(generator, matcher, occurences -> {
            Assert.assertEquals(1, occurences.size());
            Assert.assertEquals(0, occurences.get(0).getPosition());
            Assert.assertEquals(expectedTemplateId, occurences.get(0).getTemplateId());
        });
    }

    public static long testOnEqualGrayStrings(Generators generator, IMetaTemplateMatcher matcher,
                                              Consumer<List<Occurence>> check)
            throws TemplateAlreadyExist {
        matcher.addTemplate(generator.getGrayString());
        return Utils.matchStreamAndCountMilliseconds(matcher, generator.getGrayString(),
                check);
    }

    public static long testTemplateIsLargerThanStream(Generators generator, IMetaTemplateMatcher matcher)
            throws TemplateAlreadyExist {
        return testTemplateIsLargerThanStream(generator, matcher, occurences -> {
            Assert.assertEquals(0, occurences.size());
        });
    }

    public static long testTemplateIsLargerThanStream(Generators generator, IMetaTemplateMatcher matcher,
                                                      Consumer<List<Occurence>> check)
            throws TemplateAlreadyExist {
        matcher.addTemplate(Generators.getForSize((byte) (generator.getSizeCode() + 1)).getGrayString());
        return Utils.matchStreamAndCountMilliseconds(matcher, generator.getGrayString(),
                check);
    }

    public static long testOnGrayStringDifferentLength(Generators generator, IMetaTemplateMatcher matcher)
            throws TemplateAlreadyExist {
        return testOnGrayStringDifferentLength(generator, matcher, occurences -> {
            Assert.assertEquals(2, occurences.size());
            Collections.sort(occurences);
            Assert.assertEquals(0, occurences.get(0).getPosition());
            Assert.assertEquals(generator.getGrayString().length() / 2 + 1, occurences.get(1).getPosition());
        });
    }

    public static long testOnGrayStringDifferentLength(Generators generator, IMetaTemplateMatcher matcher,
                                                       Consumer<List<Occurence>> check)
            throws TemplateAlreadyExist {
        matcher.addTemplate(Generators.getForSize((byte) (generator.getSizeCode() - 1)).getGrayString());
        return Utils.matchStreamAndCountMilliseconds(matcher, generator.getGrayString(),
                check);
    }

    public static long testOnGrayStringDifferentLength2(Generators generator, IMetaTemplateMatcher matcher)
            throws TemplateAlreadyExist {
        return testOnGrayStringDifferentLength2(generator, matcher, occurences -> {
            Assert.assertEquals(4, occurences.size());
            Collections.sort(occurences);
            int length = generator.getGrayString().length() + 1;
            Assert.assertEquals(0, occurences.get(0).getPosition());
            Assert.assertEquals((length) / 4, occurences.get(1).getPosition());
            Assert.assertEquals(length / 2, occurences.get(2).getPosition());
            Assert.assertEquals(3 * (length) / 4, occurences.get(3).getPosition());
        });
    }

    public static long testOnGrayStringDifferentLength2(Generators generator, IMetaTemplateMatcher matcher,
                                                        Consumer<List<Occurence>> check)
            throws TemplateAlreadyExist {
        matcher.addTemplate(Generators.getForSize((byte) (generator.getSizeCode() - 2)).getGrayString());
        return Utils.matchStreamAndCountMilliseconds(matcher, generator.getGrayString(),
                check);
    }

    public static long testOnManySameChars(Generators generator, IMetaTemplateMatcher matcher)
            throws TemplateAlreadyExist {
        return testOnManySameChars(generator, matcher, occurences -> {
            Assert.assertEquals(4, occurences.size());
            Collections.sort(occurences);
            int length = generator.getSize();
            Assert.assertEquals(0, occurences.get(0).getPosition());
            Assert.assertEquals(length, occurences.get(1).getPosition());
            Assert.assertEquals(length * 2, occurences.get(2).getPosition());
            Assert.assertEquals(3 * length, occurences.get(3).getPosition());
        });
    }

    public static long testOnManySameChars(Generators generator, IMetaTemplateMatcher matcher,
                                           Consumer<List<Occurence>> check)
            throws TemplateAlreadyExist {
        matcher.addTemplate(generator.getAllEqualsCharsInsteadLast());
        return Utils.matchStreamAndCountMilliseconds(matcher, generator.getManyEqualChars(),
                check);
    }

    public static long testOnEqualCharsStreamIsTwoTemplates(Generators generator, IMetaTemplateMatcher matcher)
            throws TemplateAlreadyExist {
        return testOnEqualCharsStreamIsTwoTemplates(generator, matcher, occurences -> {
            int expectedSize = generator.getSize() / 2 + 1;
            Assert.assertEquals(expectedSize, occurences.size());
            Collections.sort(occurences);
            for (int i = 0; i < expectedSize; i++) {
                Assert.assertEquals(i, occurences.get(i).getPosition());
                Assert.assertEquals(expectedTemplateId, occurences.get(i).getTemplateId());
            }
        });
    }

    public static long testOnEqualCharsStreamIsTwoTemplates(Generators generator, IMetaTemplateMatcher matcher,
                                                            Consumer<List<Occurence>> check)
            throws TemplateAlreadyExist {
        matcher.addTemplate(generator.getHalfLengthEqualChars());
        return Utils.matchStreamAndCountMilliseconds(matcher, generator.getAllEqualsChars(),
                check);
    }

    @Before
    public void setUp() throws TemplateAlreadyExist {
        matcher = factory.generate();
    }

    @Test
    public void testOnEmptyTemplatesWithStringOfEqualChars() throws TemplateAlreadyExist {
        for (byte i = 1; i < MAX_TEST_SIZE; i++) {
            testOnEmptyTemplatesWithStringOfEqualChars(Generators.getForSize(i), factory.generate());
        }
    }

    @Test
    public void testOnSameStringsOfEqualChars() throws TemplateAlreadyExist {
        matcher.addTemplate("");
        Assert.assertEquals(0, matcher.matchStream(new EmptyStream()).size());
        for (byte i = 1; i < MAX_TEST_SIZE; i++) {
            testOnSameStringsOfEqualChars(Generators.getForSize(i), factory.generate());
        }
    }

    @Test
    public void testOnOneCharTemplatesWithStringOfEqualChars() throws TemplateAlreadyExist {
        for (byte i = 1; i < MAX_TEST_SIZE; i++) {
            testOnOneCharTemplatesWithStringOfEqualChars(Generators.getForSize(i), factory.generate());
        }
    }

    @Test
    public void testOnStringDifferentInLastChar() throws TemplateAlreadyExist {
        for (byte i = 1; i < MAX_TEST_SIZE; i++) {
            testOnStringDifferentInLastChar(Generators.getForSize(i), factory.generate());
        }
    }

    @Test
    public void testOnEqualGrayStrings() throws TemplateAlreadyExist {
        for (byte i = 1; i < MAX_TEST_SIZE; i++) {
            testOnEqualGrayStrings(Generators.getForSize(i), factory.generate());
        }
    }

    @Test
    public void testTemplateIsLargerThanStream() throws TemplateAlreadyExist {
        for (byte i = 1; i < MAX_TEST_SIZE; i++) {
            testTemplateIsLargerThanStream(Generators.getForSize(i), factory.generate());
        }
    }

    @Test
    public void testOnStringDifferentInFirstChar() throws TemplateAlreadyExist {
        for (byte i = 1; i < MAX_TEST_SIZE; i++) {
            testOnStringDifferentInFirstChar(Generators.getForSize(i), factory.generate());
        }
    }

    @Test
    public void testOnGrayStringDifferentLength() throws TemplateAlreadyExist {
        for (byte i = 2; i < MAX_TEST_SIZE; i++) {
            testOnGrayStringDifferentLength(Generators.getForSize(i), factory.generate());
        }
    }

    @Test
    public void testOnGrayStringDifferentLength2() throws TemplateAlreadyExist {
        for (byte i = 3; i < MAX_TEST_SIZE; i++) {
            testOnGrayStringDifferentLength2(Generators.getForSize(i), factory.generate());
        }
    }

    @Test
    public void testOnManySameChars() throws TemplateAlreadyExist {
        for (byte i = 1; i < MAX_TEST_SIZE - 1; i++) {
            testOnManySameChars(Generators.getForSize(i), factory.generate());
        }
    }

    @Test
    public void simpleTest() throws TemplateAlreadyExist {
        matcher.addTemplate("qwert");
        List<Occurence> result = matcher.matchStream(new StringStream("rfgrfdvsdqwertyfvbcgbqwerdgdfsqwertyu"));
        Assert.assertEquals(2, result.size());
        Collections.sort(result);
        Assert.assertEquals(9, result.get(0).getPosition());
        Assert.assertEquals(30, result.get(1).getPosition());
    }

    @Test
    public void testCanAddManyStreams() throws TemplateAlreadyExist {
        matcher.addTemplate("qwert");
        List<Occurence> result = matcher.matchStream(new StringStream("rfgrfdvsdqwertyfvbcgbqwerdgdfsqwertyu"));
        Assert.assertEquals(2, result.size());
        Collections.sort(result);
        Assert.assertEquals(9, result.get(0).getPosition());
        Assert.assertEquals(30, result.get(1).getPosition());
        result = matcher.matchStream(new StringStream("qwerty"));
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(0, result.get(0).getPosition());
        Assert.assertEquals(expectedTemplateId, result.get(0).getTemplateId());
        result = matcher.matchStream(new StringStream("qwrty"));
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testOnEqualCharsStreamIsTwoTemplates() throws TemplateAlreadyExist {
        for (byte i = 5; i < MAX_TEST_SIZE - 1; i++) {
            testOnEqualCharsStreamIsTwoTemplates(Generators.getForSize(i), factory.generate());
        }
    }

    @Test(expected = RuntimeException.class)
    public void testThrowsExceptionOnBrokenStream() throws TemplateAlreadyExist {
        matcher.addTemplate("a");
        matcher.matchStream(new BadStream());
    }

}
