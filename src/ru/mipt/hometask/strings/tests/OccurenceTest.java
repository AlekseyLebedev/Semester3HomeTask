package ru.mipt.hometask.strings.tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.mipt.hometask.strings.Occurence;

public class OccurenceTest {
    private Occurence first;
    private Occurence second;

    @Before
    public void setUp() throws Exception {
        first = new Occurence(7, "qwerty", 6);
        second = new Occurence(5, 11);
    }

    @Test
    public void testGetPosition() throws Exception {
        Assert.assertEquals(7, first.getPosition());
        Assert.assertEquals(5, second.getPosition());
    }

    @Test
    public void testGetTemplateId() throws Exception {
        Assert.assertEquals(6, first.getTemplateId());
        Assert.assertEquals(11, second.getTemplateId());

    }

    @Test
    public void testGetTemplate1() throws Exception {
        Assert.assertEquals("qwerty", first.getTemplate());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetTemplate2() throws Exception {
        second.getTemplate();
    }

    @Test
    public void testIsTemplateStringEnabled() throws Exception {
        Assert.assertEquals(true, first.isTemplateStringEnabled());
        Assert.assertEquals(false, second.isTemplateStringEnabled());
    }
}
