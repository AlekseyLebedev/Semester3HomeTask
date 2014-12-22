package ru.mipt.hometask.strings.interfaces;

import ru.mipt.hometask.strings.exceptions.EmptyStreamException;

public interface ICharStream {
    public char getChar() throws EmptyStreamException;

    public boolean isEmpty();
}
