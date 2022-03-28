package com.wit.challenge.calculator.services;

import java.math.BigDecimal;

/**
 * The calculation contract
 */
public interface ICalculator {
    Float sum(BigDecimal a, BigDecimal b);
    Float subtract(BigDecimal a, BigDecimal b);
    Float multiply(BigDecimal a, BigDecimal b);
    Float division(BigDecimal a, BigDecimal b);
}
