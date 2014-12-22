package ru.mipt.hometask.strings.exceptions;

public class EmptyStreamException extends StringsLibraryException {
    public EmptyStreamException() {
        super("The end of stream has been already reached");
    }

    public EmptyStreamException(String message) {
        super(message);
    }

    public EmptyStreamException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyStreamException(Throwable cause) {
        super(cause);
    }
}
