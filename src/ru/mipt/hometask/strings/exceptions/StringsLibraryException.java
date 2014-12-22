package ru.mipt.hometask.strings.exceptions;

public class StringsLibraryException extends Exception {
    public StringsLibraryException() {
    }

    public StringsLibraryException(String message) {
        super(message);
    }

    public StringsLibraryException(String message, Throwable cause) {
        super(message, cause);
    }

    public StringsLibraryException(Throwable cause) {
        super(cause);
    }
}
