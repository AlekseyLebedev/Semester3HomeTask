package ru.mipt.hometask.strings.tests;

import ru.mipt.hometask.strings.NaiveTemplateMatcher;
import ru.mipt.hometask.strings.SingleTemplateMatcher;
import ru.mipt.hometask.strings.StaticTemplateMatcher;
import ru.mipt.hometask.strings.exceptions.TemplateAlreadyExist;
import ru.mipt.hometask.strings.interfaces.IMetaTemplateMatcher;

import java.io.PrintStream;
import java.util.List;

@FunctionalInterface
interface TestRunner {
    public long run(Generators generators, IMetaTemplateMatcher matcher) throws TemplateAlreadyExist;
}

public class PerfomanceTests {
    public static final int MAX_TIME = 10000;
    public static final int CONSOLE_WIDTH = 120;
    public static final int META_TEMPLATE_MATCHERS = 3;
    public static final String NO_TIME_MESSAGE = "*";
    private long[] singleTemplateTimes = new long[3];

    public void run() throws TemplateAlreadyExist {
        run(System.out);
    }

    public void run(PrintStream out) throws TemplateAlreadyExist {
        out.println("     ==Tests with one template==");
        SingleTemplateMatchersTests.expectedTemplateId = 0;
        TablePrinter printer = new TablePrinter(4, CONSOLE_WIDTH, new String[]{
                "n", "NaiveTemplateMatcher", "SingleTemplateMatcher", "StaticTemplateMatcher"}, out);
        out.println("\"\" in aaaaaaaaaaaaaaa...");
        runSingleTemplateTests(printer, SingleTemplateMatchersTests::testOnEmptyTemplatesWithStringOfEqualChars,
                (Generators generator, IMetaTemplateMatcher matcher) -> SingleTemplateMatchersTests.
                        testOnEmptyTemplatesWithStringOfEqualChars(generator, matcher, null), 17, true);
        out.println("a in aaaaaaaaaaaaaaa...");
        runSingleTemplateTests(printer, SingleTemplateMatchersTests::testOnOneCharTemplatesWithStringOfEqualChars,
                (Generators generator, IMetaTemplateMatcher matcher) -> SingleTemplateMatchersTests.
                        testOnOneCharTemplatesWithStringOfEqualChars(generator, matcher, null), 17, true);
        out.println("aaaaaaaaaaaa... in aaaaaaaaaaaaaaa... (equals)");
        runSingleTemplateTests(printer, SingleTemplateMatchersTests::testOnSameStringsOfEqualChars,
                (Generators generator, IMetaTemplateMatcher matcher) -> SingleTemplateMatchersTests.
                        testOnSameStringsOfEqualChars(generator, matcher, null), 17, true);
        out.println("aaa..aaa in aaa...aab (equal length)");
        runSingleTemplateTests(printer, SingleTemplateMatchersTests::testOnStringDifferentInLastChar,
                (Generators generator, IMetaTemplateMatcher matcher) -> SingleTemplateMatchersTests.
                        testOnStringDifferentInLastChar(generator, matcher, null), 17, true);
        out.println("aaa..aaa in baa...aaa (equal length)");
        runSingleTemplateTests(printer, SingleTemplateMatchersTests::testOnStringDifferentInFirstChar,
                (Generators generator, IMetaTemplateMatcher matcher) -> SingleTemplateMatchersTests.
                        testOnStringDifferentInFirstChar(generator, matcher, null), 17, true);
        out.println("aaab in aaabaaabaaabaaab (given size is template, stream 4 times larger)");
        runSingleTemplateTests(printer, SingleTemplateMatchersTests::testOnManySameChars,
                (Generators generator, IMetaTemplateMatcher matcher) -> SingleTemplateMatchersTests.
                        testOnManySameChars(generator, matcher, null), 17, true);
        out.println("aaaa in aaaaaaaa (same chars, stream two times larger)");
        runSingleTemplateTests(printer, SingleTemplateMatchersTests::testOnEqualCharsStreamIsTwoTemplates,
                (Generators generator, IMetaTemplateMatcher matcher) -> SingleTemplateMatchersTests.
                        testOnEqualCharsStreamIsTwoTemplates(generator, matcher, null), 17, true);
        out.println("abacaba in abacaba (equal)");
        runSingleTemplateTests(printer, SingleTemplateMatchersTests::testOnEqualGrayStrings,
                (Generators generator, IMetaTemplateMatcher matcher) -> SingleTemplateMatchersTests.
                        testOnEqualGrayStrings(generator, matcher, null), 23, false);
        out.println("abacaba in aba (template larger)");
        runSingleTemplateTests(printer, SingleTemplateMatchersTests::testTemplateIsLargerThanStream,
                (Generators generator, IMetaTemplateMatcher matcher) -> SingleTemplateMatchersTests.
                        testTemplateIsLargerThanStream(generator, matcher, null), 22, false);
        out.println("aba in abacaba (stream larger)");
        runSingleTemplateTests(printer, SingleTemplateMatchersTests::testOnGrayStringDifferentLength,
                (Generators generator, IMetaTemplateMatcher matcher) -> SingleTemplateMatchersTests.
                        testOnGrayStringDifferentLength(generator, matcher, null), 23, false);
        out.println("aba in abacabadabacaba (stream ~4 times larger)");
        runSingleTemplateTests(printer, SingleTemplateMatchersTests::testOnGrayStringDifferentLength2,
                (Generators generator, IMetaTemplateMatcher matcher) -> SingleTemplateMatchersTests.
                        testOnGrayStringDifferentLength2(generator, matcher, null), 23, false);

        out.println("     ==Tests with many templates==");
        SingleTemplateMatchersTests.expectedTemplateId = 0;
        printer = new TablePrinter(META_TEMPLATE_MATCHERS + 1, CONSOLE_WIDTH, new String[]{
                "n", "NaiveTemplateMatcher", "StaticTemplateMatcher", ""}, out);
        for (int test = 0; test < MetaTemplateMatchersTest.TESTS_COUNT; test++) {
            out.println("Test set " + test);
            testSetForMetaTemplate(printer, test, (byte) 16);
        }
    }

    private void testSetForMetaTemplate(TablePrinter printer, int test, byte maxSize) {
        final long[][] times = new long[META_TEMPLATE_MATCHERS][2];
        printer.printHead();
        for (byte size = 10; size < maxSize; size++) {
            List<String> templates = MetaTemplateMatchersTest.getTemplates(size, test);
            List<String> streams = MetaTemplateMatchersTest.getStreams(size, test);
            final int[] result = new int[META_TEMPLATE_MATCHERS];
            runMetaTemplateTest(times, templates, streams, new NaiveTemplateMatcher(), 0, result);
            runMetaTemplateTest(times, templates, streams, new StaticTemplateMatcher(), 1, result);
            int templatesSize = 0;
            for (String template : templates) {
                templatesSize += template.length();
            }
            int streamsSize = 0;
            for (String stream : streams) {
                streamsSize += stream.length();
            }
            printer.print("Code" + size + ", occ.");
            for (int i = 0; i < META_TEMPLATE_MATCHERS; i++) {
                printer.print(result[i]);
            }
            printer.print("T" + templates.size() + ":" + templatesSize);
            for (int i = 0; i < META_TEMPLATE_MATCHERS; i++) {
                printTime(printer, times[i][0]);
            }
            printer.print("S" + streams.size() + ":" + streamsSize);
            for (int i = 0; i < META_TEMPLATE_MATCHERS; i++) {
                printTime(printer, times[i][1]);
            }

        }
    }

    private void printTime(TablePrinter printer, long time) {
        if (time >= 0) {
            printer.print(time);
        } else {
            printer.print(NO_TIME_MESSAGE);
        }
    }

    private void runMetaTemplateTest(long[][] times, List<String> templates, List<String> streams,
                                     IMetaTemplateMatcher matcher, int index, int[] result) {
        if (times[index][0] >= 0 && times[index][1] >= 0 && times[index][0] < MAX_TIME && times[index][1] < MAX_TIME) {
            times[index][0] = Utils.runAndCountMilliseconds(() -> {
                try {
                    for (String template : templates) {
                        matcher.addTemplate(template);
                    }
                } catch (TemplateAlreadyExist templateAlreadyExist) {
                    templateAlreadyExist.printStackTrace();
                }
            });
            times[index][1] = 0;
            for (String stream : streams) {
                times[index][1] += Utils.matchStreamAndCountMilliseconds(matcher, stream, occurences ->
                        result[index] += occurences.size());
            }
        } else {
            times[index][0] = times[index][1] = -1;
        }
    }

    private void runSingleTemplateTests(TablePrinter printer, TestRunner checker, TestRunner runner, int max,
                                        boolean regularSize) throws TemplateAlreadyExist {
        for (int i = 0; i < 3; i++) {
            singleTemplateTimes[i] = 0;
        }
        printer.printHead();
        for (byte i = 10; i < SingleTemplateMatchersTests.MAX_TEST_SIZE; i++) {
            writeOneSingleTemplateTest(printer, checker, i, regularSize);
        }
        for (byte i = SingleTemplateMatchersTests.MAX_TEST_SIZE; i < max; i++) {
            writeOneSingleTemplateTest(printer, runner, i, regularSize);
        }
    }

    private void writeOneSingleTemplateTest(TablePrinter printer, TestRunner work, byte size, boolean regularSize)
            throws TemplateAlreadyExist {
        Generators generator = Generators.getForSize(size);
        if (regularSize) {
            printer.print(generator.getSize());
        } else {
            printer.print(generator.getGrayString().length());
        }
        writeOneSingleTemplateTest(printer, work, generator, new NaiveTemplateMatcher(), 0);
        writeOneSingleTemplateTest(printer, work, generator, new SingleTemplateMatcher(), 1);
        writeOneSingleTemplateTest(printer, work, generator, new StaticTemplateMatcher(), 2);
    }

    private void writeOneSingleTemplateTest(TablePrinter printer, TestRunner work, Generators generator,
                                            IMetaTemplateMatcher matcher, int i) throws TemplateAlreadyExist {
        if (singleTemplateTimes[i] > MAX_TIME) {
            printer.print(NO_TIME_MESSAGE);
        } else {
            long time = work.run(generator, matcher);
            printer.print(time);
            singleTemplateTimes[i] = time;
        }
    }
}

class TablePrinter {
    public static final String COLUMN_SEPARATOR = "|";
    private int columnCount;
    private int consoleWidth;
    private int x, y;
    private int columnWidth;
    private String[] headers;
    private PrintStream stream;

    public TablePrinter(int columnCount, int consoleWidth, String[] headers) {
        this(columnCount, consoleWidth, headers, System.out);
    }

    public TablePrinter(int columnCount, int consoleWidth, String[] headers, PrintStream out) {
        this.columnCount = columnCount;
        this.consoleWidth = consoleWidth;
        if (headers.length != columnCount) {
            throw new IllegalArgumentException("Wrong column count");
        }
        this.headers = headers;
        this.stream = out;
        columnWidth = (consoleWidth - 1) / columnCount - 1;
        x = 0;
        y = 0;
    }

    public void printHead() {
        printLine();
        for (String item : headers) {
            print(item);
        }
    }

    public void print(String value) {
        if (value.length() > columnWidth) {
            value = value.substring(0, columnWidth);
        }
        stream.print(COLUMN_SEPARATOR);
        stream.print(value);
        for (int i = value.length(); i < columnWidth; i++) {
            stream.print(" ");
        }
        ++x;
        if (x == columnCount) {
            ++y;
            x = 0;
            stream.println(COLUMN_SEPARATOR);
            printLine();
        }
    }

    private void printLine() {
        int width = (columnWidth + 1) * columnCount + 1;
        for (int i = 0; i < width; i++) {
            stream.print("-");
        }
        stream.println();
    }


    public void print(long size) {
        print(((Long) size).toString());
    }
}
