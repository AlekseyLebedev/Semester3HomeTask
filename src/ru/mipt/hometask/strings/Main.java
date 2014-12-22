package ru.mipt.hometask.strings;

import org.junit.Assert;
import ru.mipt.hometask.strings.exceptions.TemplateAlreadyExist;
import ru.mipt.hometask.strings.interfaces.IMetaTemplateMatcher;
import ru.mipt.hometask.strings.tests.PerfomanceTests;

import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) throws TemplateAlreadyExist {
        IMetaTemplateMatcher matcher = new StaticTemplateMatcher();
        matcher.addTemplate("qwert");
        List<Occurence> result = matcher.matchStream(new StringStream("rfgrfdvsdqwertyfvbcgbqwerdgdfsqwertyu"));
        Assert.assertEquals(2, result.size());
        Collections.sort(result);
        for (Occurence o : result) {
            System.out.println(o.getPosition() + " " + o.getPosition());
        }
        //Assert.assertEquals(9, result.get(0).getPosition());
        //Assert.assertEquals(30, result.get(1).getPosition());
        //new PerfomanceTests().run();
    }
}
