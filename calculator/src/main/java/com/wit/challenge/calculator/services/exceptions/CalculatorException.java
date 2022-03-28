package com.wit.challenge.calculator.services.exceptions;

public class CalculatorException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CalculatorException(String msg) {
        super(msg);
    }

    public CalculatorException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
