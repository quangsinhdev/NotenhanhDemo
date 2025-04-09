package com.notenhanh.exception.note;

public class TextnoteNotFoundException extends RuntimeException {
    public TextnoteNotFoundException(Long id) {
        super("Textnote with id " + id + " not found");
    }
    public TextnoteNotFoundException(String url) {
        super("Textnote with url: " + url + " not found");
    }
}
