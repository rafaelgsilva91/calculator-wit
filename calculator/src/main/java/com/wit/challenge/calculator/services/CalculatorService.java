package com.wit.challenge.calculator.services;

import com.wit.challenge.calculator.enums.EnumOperator;
import com.wit.challenge.calculator.services.exceptions.CalculatorException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculatorService implements ICalculator {
    Logger logger = LoggerFactory.getLogger(CalculatorService.class);

    private BigDecimal numberA, numberB;
    private static RoundingMode roundingMode = RoundingMode.HALF_EVEN; // Config arbitraryprecision with HALF_EVEN strategy
    private static int precision = 2; // Config decimals precision 2 float point

    public Float processCalc(String a, String b, EnumOperator operator) {
        this.numberA = validateNumbers(a);
        this.numberB = validateNumbers(b);
        if (this.numberA != null && this.numberB != null) {
            if (operator == EnumOperator.SUM) {
                return sum(this.numberA, this.numberB);
            } else if (operator == EnumOperator.SUBTRACT) {
                return subtract(this.numberA, this.numberB);
            } else if (operator == EnumOperator.MULTIPLY) {
                return multiply(this.numberA, this.numberB);
            } else {
                return division(this.numberA, this.numberB);
            }
        }
        return null;
    }

    /**
     * Validate numbers
     *
     * @param sAux - the number to be validated
     * @return BigDecimal
     */
    private BigDecimal validateNumbers(String sAux) {
        try {
            return new BigDecimal(sAux);
        } catch (NumberFormatException e) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("exception", "Send a valid numbers!");
            throw new CalculatorException(jsonObject.toString());
        }
    }

    @Override
    public Float sum(BigDecimal a, BigDecimal b) {
        return a.add(b).setScale(precision, roundingMode).floatValue();
    }

    @Override
    public Float subtract(BigDecimal a, BigDecimal b) {
        return a.subtract(b).setScale(precision, roundingMode).floatValue();
    }

    @Override
    public Float multiply(BigDecimal a, BigDecimal b) {
        return a.multiply(b).setScale(precision, roundingMode).floatValue();
    }

    @Override
    public Float division(BigDecimal a, BigDecimal b) {
        return a.divide(b, precision, roundingMode).floatValue();
    }
}
