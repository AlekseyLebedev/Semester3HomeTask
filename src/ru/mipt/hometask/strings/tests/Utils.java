package ru.mipt.hometask.strings.tests;

import ru.mipt.hometask.strings.Occurence;
import ru.mipt.hometask.strings.StringStream;
import ru.mipt.hometask.strings.interfaces.IMetaTemplateMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Utils {
    private static Utils ourInstance = new Utils();

    private List<String> grayStrings = new ArrayList<String>();

    private Utils() {

    }

    public static Utils getInstance() {
        return ourInstance;
    }

    public static String getGrayString(byte number) {
        return getInstance().grayString(number);
    }

    public static long runAndCountMilliseconds(Runnable work) {
        long startTime = System.currentTimeMillis();
        work.run();
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    public static long matchStreamAndCountMilliseconds(IMetaTemplateMatcher matcher, String input,
                                                       Consumer<List<Occurence>> check) {
        StringStream stream = new StringStream(input);
        List<Occurence> occurences;
        long startTime = System.currentTimeMillis();
        occurences = matcher.matchStream(stream);
        long endTime = System.currentTimeMillis();
        if (check != null) {
            check.accept(occurences);
        }
        return endTime - startTime;
    }

    public String grayString(byte number) {
        if (number < 0) {
            throw new IllegalStateException("Only positive numbers ase supported");
        }
        if (grayStrings.size() == 0) {
            grayStrings.add("a");
        }
        while (grayStrings.size() <= number) {
            int index = grayStrings.size();
            char symbol;
            if (number < 26) {
                symbol = (char) ('a' + index);
            } else {
                if (number < 26 * 2) {
                    symbol = (char) ('A' + index - 26);
                } else {
                    if (number < 26 * 2 + 10) {
                        symbol = (char) ('0' + index - 26 * 2);
                    } else {
                        throw new IllegalArgumentException(
                                "Only numbers less than " + (26 * 2 + 10) + " are supported");
                    }
                }
            }
            grayStrings.add(grayStrings.get(index - 1) + symbol + grayStrings.get(index - 1));
        }
        return grayStrings.get(number);
    }
}
