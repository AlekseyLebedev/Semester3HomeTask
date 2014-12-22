package ru.mipt.hometask.strings.tests;

import java.util.ArrayList;
import java.util.List;

public class Generators {
    public static final int SIZE_CODE_BARRIER = 10;
    public static final int SIZE_CODE_BASIS = 10;
    private static List<Generators> generators = new ArrayList<>();
    private byte sizeCode;
    private int size = 1;
    private String allEqualsChars;
    private String allEqualsCharsInsteadLast;
    private String allEqualsCharsInsteadFirst;
    private String manyEqualChars;
    private String halfLengthEqualChars;

    public Generators(byte sizeCode) {
        this.sizeCode = sizeCode;
        if (sizeCode < SIZE_CODE_BARRIER) {
            size = sizeCode;
        } else {
            size = SIZE_CODE_BASIS;
        }
        for (int i = SIZE_CODE_BARRIER; i < sizeCode; ++i) {
            size *= SIZE_CODE_BASIS;
        }
    }

    public static Generators getForSize(byte sizeCode) {
        if (sizeCode < 0) {
            throw new IllegalArgumentException("Wrong argument");
        }
        while (generators.size() <= sizeCode) {
            generators.add(null);
        }
        Generators value = generators.get(sizeCode);
        if (value == null) {
            generators.set(sizeCode, new Generators(sizeCode));
            return generators.get(sizeCode);
        } else {
            return value;
        }
    }

    public byte getSizeCode() {
        return sizeCode;
    }

    public String getAllEqualsChars() {
        if (allEqualsChars == null) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < size; i++) {
                builder.append("a");
            }
            allEqualsChars = builder.toString();
        }
        return allEqualsChars;
    }

    public String getAllEqualsCharsInsteadLast() {
        if (allEqualsCharsInsteadLast == null) {
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i < size; i++) {
                builder.append("a");
            }
            builder.append("b");
            allEqualsCharsInsteadLast = builder.toString();
        }
        return allEqualsCharsInsteadLast;
    }

    public String getAllEqualsCharsInsteadFirst() {
        if (allEqualsCharsInsteadFirst == null) {
            StringBuilder builder = new StringBuilder();
            builder.append("b");
            for (int i = 1; i < size; i++) {
                builder.append("a");
            }
            allEqualsCharsInsteadFirst = builder.toString();
        }
        return allEqualsCharsInsteadFirst;
    }

    public String getOneCharString() {
        return "a";
    }

    public String getGrayString() {
        return Utils.getGrayString(sizeCode);
    }

    public String getEmptyString() {
        return "";
    }

    public int getSize() {
        return size;
    }

    public String getManyEqualChars() {
        if (manyEqualChars == null) {
            manyEqualChars = getAllEqualsCharsInsteadLast();
            manyEqualChars = manyEqualChars + manyEqualChars + manyEqualChars + manyEqualChars;
        }
        return manyEqualChars;
    }

    public String getHalfLengthEqualChars() {
        if (halfLengthEqualChars == null) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < size; i += 2) {
                builder.append("a");
            }
            halfLengthEqualChars = builder.toString();
        }
        return halfLengthEqualChars;
    }
}
