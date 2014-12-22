package ru.mipt.hometask.strings;

import ru.mipt.hometask.strings.exceptions.TemplateAlreadyExist;
import ru.mipt.hometask.strings.tests.PerfomanceTests;

public class Main {
    public static void main(String[] args) throws TemplateAlreadyExist {
        new PerfomanceTests().run();
    }
}
