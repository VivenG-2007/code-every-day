package com.example.demo.exception;

public class DuplicateResouseException extends Exception{
    public DuplicateResouseException(String message){
        super("Email already exists");
    }
}
