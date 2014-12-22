package ru.mipt.hometask.strings.tests;

import ru.mipt.hometask.strings.exceptions.EmptyStreamException;
import ru.mipt.hometask.strings.interfaces.ICharStream;

public class EmptyStream implements ICharStream {
    @Override
    public char getChar() throws EmptyStreamException {
        throw new EmptyStreamException();
    }

    @Override
    public boolean isEmpty() {
        return true;
    }
}
