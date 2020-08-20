package com.exercise.mybnb.exception;

public class ActionNotAllowedException extends RuntimeException{
    public ActionNotAllowedException(String message) {
        super(message);
    }
}
