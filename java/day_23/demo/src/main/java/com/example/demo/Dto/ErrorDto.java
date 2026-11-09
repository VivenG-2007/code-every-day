package com.example.demo.Dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorDto {
    private LocalDateTime timestap;
    private int statuscode;
    private String error;
    private String message;
    private String path;

    public ErrorDto(LocalDateTime timestap, int statuscode, String error, String message, String path) {
        this.timestap = timestap;
        this.statuscode = statuscode;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}
