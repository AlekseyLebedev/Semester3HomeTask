package ru.mipt.hometask.strings.interfaces;

import ru.mipt.hometask.strings.exceptions.EmptyStreamException;

public interface ICharStream {
    char getChar() throws EmptyStreamException;

    boolean isEmpty();
}
