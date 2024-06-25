package com.sartorius.tma.exceptions;

public class DuplicateAttendanceException extends RuntimeException {
    public DuplicateAttendanceException(String message) {
        super(message);
    }
}
