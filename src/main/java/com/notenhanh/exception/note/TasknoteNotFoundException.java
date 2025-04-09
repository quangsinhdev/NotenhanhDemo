package com.notenhanh.exception.note;

public class TasknoteNotFoundException extends RuntimeException {
    public TasknoteNotFoundException(Long id) {
        super("Tasknote with id " + id + " not found");
    }
    public TasknoteNotFoundException(String url) {
        super("Tasknote with url: " + url + " not found");
    }
}
