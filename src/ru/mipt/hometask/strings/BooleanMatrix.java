package ru.mipt.hometask.strings;

import java.util.ArrayList;
import java.util.List;

public class BooleanMatrix {
    List<byte[]> data;
    private int height;
    private int count;

    public BooleanMatrix(int height) {
        this.height = height;
        count = height / 8 + 1;
        data = new ArrayList<>();
    }

    public boolean get(int x, int y) {
        makeLength(x);
        checkY(y);
        int row = y / 8;
        int position = y % 8;
        int value = data.get(x)[row];
        if (value < 0) {
            value += 256;
        }
        value = (value >> position) % 2;
        return value == 1;
    }

    private void checkY(int y) {
        if (y >= height) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void makeLength(int x) {
        while (x >= data.size()) {
            data.add(new byte[count]);
        }
    }

    public void set(int x, int y, boolean value) {
        checkY(y);
        makeLength(x);
        int row = y / 8;
        int position = y % 8;
        int code = data.get(x)[row];
        if (code < 0) {
            code += 256;
        }
        code = ((code >> position + 1 << 1) + (value ? 1 : 0) << position) + code % (1 << position);
        if (code > 127) {
            code -= 256;
        }
        data.get(x)[row] = (byte) code;
    }
}