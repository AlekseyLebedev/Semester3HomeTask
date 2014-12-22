package ru.mipt.hometask.strings;

import org.junit.Assert;
import ru.mipt.hometask.strings.exceptions.TemplateAlreadyExist;
import ru.mipt.hometask.strings.interfaces.IMetaTemplateMatcher;
import ru.mipt.hometask.strings.tests.PerfomanceTests;

import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) throws TemplateAlreadyExist {
        IMetaTemplateMatcher matcher = new NaiveTemplateMatcher();
        matcher.addTemplate("****");
        matcher.addTemplate("aab");
        List<Occurence> result = matcher.matchStream(new StringStream("aabaaabaaabaaab"));
        for (Occurence o : result) {
            System.out.println(o.getPosition() + " " + o.getTemplateId());
        }
        //new PerfomanceTests().run();
    }
}
