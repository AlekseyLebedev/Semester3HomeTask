package ru.mipt.hometask.strings;

import ru.mipt.hometask.strings.exceptions.EmptyStreamException;
import ru.mipt.hometask.strings.interfaces.ICharStream;

public final class StringStream implements ICharStream {
    private String text;
    private int index = 0;

    public StringStream(String text) {
        this.text = text;
    }

    @Override
    public char getChar() throws EmptyStreamException {
        if (isEmpty()) {
            throw new EmptyStreamException();
        }
        return text.charAt(index++);
    }

    @Override
    public boolean isEmpty() {
        return index == text.length();
    }
}
