package ru.mipt.hometask.strings.exceptions;

public class TemplateAlreadyExist extends StringsLibraryException {
    public TemplateAlreadyExist() {
    }

    public TemplateAlreadyExist(String message) {
        super(message);
    }

    public TemplateAlreadyExist(String message, Throwable cause) {
        super(message, cause);
    }

    public TemplateAlreadyExist(Throwable cause) {
        super(cause);
    }
}
