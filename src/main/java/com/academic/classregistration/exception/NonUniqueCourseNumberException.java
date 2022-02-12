package com.academic.classregistration.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class NonUniqueCourseNumberException extends DataIntegrityViolationException {
    public NonUniqueCourseNumberException(String courseNumber) {
        super("Unable to save Course. Course Number " + courseNumber + " already exists.");
    }
}
