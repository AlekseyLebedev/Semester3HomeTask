package ru.mipt.hometask.strings.tests;

import org.junit.Assert;
import org.junit.Test;
import ru.mipt.hometask.strings.BooleanMatrix;
import ru.mipt.hometask.strings.Occurence;
import ru.mipt.hometask.strings.StringStream;
import ru.mipt.hometask.strings.WildcardSingleTemplateMatcher;
import ru.mipt.hometask.strings.exceptions.TemplateAlreadyExist;

import java.util.Collections;
import java.util.List;

public class WildcardSingleTemplateMatcherTest {
    @Test
    public void testWildcardWithGrayStringWithWildcardInCenter() throws TemplateAlreadyExist {
        WildcardSingleTemplateMatcher matcher = new WildcardSingleTemplateMatcher();
        matcher.addTemplate("aba?aba");
        int lastSize = 0;
        for (byte size = 2; size < 10; size++) {
            List<Occurence> occurences = matcher.matchStream(new StringStream(
                    Generators.getForSize(size).getGrayString()));
            lastSize = 2 * lastSize + 1;
            Assert.assertEquals(lastSize, occurences.size());
            Collections.sort(occurences);
            for (int i = 0; i < occurences.size(); i++) {
                Assert.assertEquals(i * 4, occurences.get(i).getPosition());
            }
        }
        Assert.assertEquals(0, matcher.matchStream(new StringStream("aaaaaaaaaaaaaqwery")).size());
    }

    @Test
    public void testWildcardWithAWildcardA() throws TemplateAlreadyExist {
        WildcardSingleTemplateMatcher matcher = new WildcardSingleTemplateMatcher();
        matcher.addTemplate("a?a");
        for (byte size = 2; size < 15; size++) {
            Generators generator = Generators.getForSize(size);
            List<Occurence> occurences = matcher.matchStream(new StringStream(
                    generator.getAllEqualsChars()));
            Assert.assertEquals(generator.getSize() - 2, occurences.size());
            Collections.sort(occurences);
            for (int i = 0; i < occurences.size(); i++) {
                Assert.assertEquals(i, occurences.get(i).getPosition());
            }
            if (size > 5) {
                occurences = matcher.matchStream(new StringStream(
                        generator.getAllEqualsCharsInsteadLast()));
                Assert.assertEquals(generator.getSize() - 3, occurences.size());
                Collections.sort(occurences);
                for (int i = 0; i < occurences.size(); i++) {
                    Assert.assertEquals(i, occurences.get(i).getPosition());
                }
                occurences = matcher.matchStream(new StringStream(
                        generator.getAllEqualsCharsInsteadFirst()));
                Assert.assertEquals(generator.getSize() - 3, occurences.size());
                Collections.sort(occurences);
                for (int i = 0; i < occurences.size(); i++) {
                    Assert.assertEquals(i + 1, occurences.get(i).getPosition());
                }
            }
        }
    }

    @Test
    public void testWildcardWithOneCharInSenterOfWildCards() throws TemplateAlreadyExist {
        WildcardSingleTemplateMatcher matcher = new WildcardSingleTemplateMatcher();
        matcher.addTemplate("??a??");
        Assert.assertEquals(1, matcher.matchStream(new StringStream("aaaaa")).size());
        for (byte size = 4; size < 15; size++) {
            Generators generator = Generators.getForSize(size);
            List<Occurence> occurences = matcher.matchStream(new StringStream(
                    generator.getAllEqualsChars()));
            Assert.assertEquals(generator.getSize() - 4, occurences.size());
            Collections.sort(occurences);
            for (int i = 0; i < occurences.size(); i++) {
                Assert.assertEquals(i, occurences.get(i).getPosition());
            }
            occurences = matcher.matchStream(new StringStream(
                    generator.getAllEqualsCharsInsteadLast()));
            Assert.assertEquals(generator.getSize() - 4, occurences.size());
            Collections.sort(occurences);
            for (int i = 0; i < occurences.size(); i++) {
                Assert.assertEquals(i, occurences.get(i).getPosition());
            }
            occurences = matcher.matchStream(new StringStream(
                    generator.getAllEqualsCharsInsteadFirst()));
            Assert.assertEquals(generator.getSize() - 4, occurences.size());
            Collections.sort(occurences);
            for (int i = 0; i < occurences.size(); i++) {
                Assert.assertEquals(i, occurences.get(i).getPosition());
            }
        }
    }

    @Test
    public void testWildcardWithThreePairsWildCards() throws TemplateAlreadyExist {
        WildcardSingleTemplateMatcher matcher = new WildcardSingleTemplateMatcher();
        matcher.addTemplate("??a??a??");
        List<Occurence> occurences = matcher.matchStream(new StringStream(
                "bbbbaaabaaba"));
        Assert.assertEquals(2, occurences.size());
        Collections.sort(occurences);
        Assert.assertEquals(3, occurences.get(0).getPosition());
        Assert.assertEquals(4, occurences.get(1).getPosition());
        for (byte size = 8; size < 15; size++) {
            Generators generator = Generators.getForSize(size);
            occurences = matcher.matchStream(new StringStream(
                    generator.getAllEqualsChars()));
            Assert.assertEquals(generator.getSize() - 7, occurences.size());
            Collections.sort(occurences);
            for (int i = 0; i < occurences.size(); i++) {
                Assert.assertEquals(i, occurences.get(i).getPosition());
            }
            occurences = matcher.matchStream(new StringStream(
                    generator.getAllEqualsCharsInsteadLast()));
            Assert.assertEquals(generator.getSize() - 7, occurences.size());
            Collections.sort(occurences);
            for (int i = 0; i < occurences.size(); i++) {
                Assert.assertEquals(i, occurences.get(i).getPosition());
            }
            occurences = matcher.matchStream(new StringStream(
                    generator.getAllEqualsCharsInsteadFirst()));
            Assert.assertEquals(generator.getSize() - 7, occurences.size());
            Collections.sort(occurences);
            for (int i = 0; i < occurences.size(); i++) {
                Assert.assertEquals(i, occurences.get(i).getPosition());
            }
        }
    }

    @Test
    public void testWildcardWithDifficaltTemplate() throws TemplateAlreadyExist {
        WildcardSingleTemplateMatcher matcher = new WildcardSingleTemplateMatcher();
        matcher.addTemplate("a?a??qw?qw?qw?t?");
        List<Occurence> occurences = matcher.matchStream(new StringStream(
                "aaaaaaaqwrqweqwqtraaazabbqwiqwyqwqtxzzabbqwiqwyqwqtx"));
        Assert.assertEquals(2, occurences.size());
        Collections.sort(occurences);
        Assert.assertEquals(2, occurences.get(0).getPosition());
        Assert.assertEquals(20, occurences.get(1).getPosition());
        occurences = matcher.matchStream(new StringStream(
                "aaaaaaaqwrqweqwqtaaazabbqwiqwyqwqtxzzabbqwiqwyqwqtx"));
        Assert.assertEquals(2, occurences.size());
        Collections.sort(occurences);
        Assert.assertEquals(2, occurences.get(0).getPosition());
        Assert.assertEquals(19, occurences.get(1).getPosition());

    }

    @Test
    public void testBooleanMatrix() {
        for (int i = 0; i < 25; i++) {
            BooleanMatrix matrix = new BooleanMatrix(i);
            for (int j = 0; j < i; j++) {
                matrix.set(j, j, true);
                matrix.set(2 * i - j - 1, j, true);
            }
            for (int j = 0; j < 2 * i - 1; j++) {
                for (int k = 0; k < i; k++) {
                    Assert.assertEquals((k == j) || (2 * i - j - 1 == k), matrix.get(j, k));
                }
            }
        }
    }
}