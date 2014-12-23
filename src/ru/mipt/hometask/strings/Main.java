package ru.mipt.hometask.strings;

import ru.mipt.hometask.strings.exceptions.TemplateAlreadyExist;
import ru.mipt.hometask.strings.tests.PerfomanceTests;

import java.util.List;

public class Main {
    public static void main(String[] args) throws TemplateAlreadyExist {
        new PerfomanceTests().run();
    }

    private static void match(DynamicTemplateMatcher matcher) {
        List<Occurence> oc = matcher.matchStream(new StringStream("aaaab"));
        for (Occurence item : oc) {
            System.out.println(item.getPosition() + " " + item.getTemplateId());
        }
    }
}
