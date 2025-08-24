package com.github.Ignacio.my_Library_In_Spring.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.List;

public class ApiError {
    @Positive
    private int code;

    @NotBlank
    private String type;

    @NotBlank
    private String message;

    @NotBlank
    private String path;

    private LocalDateTime time;

    private List<ValidationDetail> details;

    public ApiError(int code, String type, String message, String path, LocalDateTime time, List<ValidationDetail> details) {
        this.code = code;
        this.type = type;
        this.message = message;
        this.path = path;
        this.time = time;
        this.details = details;
    }

    public ApiError(int code, String type, String message, String path, LocalDateTime time) {
        this.code = code;
        this.type = type;
        this.message = message;
        this.path = path;
        this.time = time;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public List<ValidationDetail> getDetails() {
        return details;
    }

    public void setDetails(List<ValidationDetail> details) {
        this.details = details;
    }
}
