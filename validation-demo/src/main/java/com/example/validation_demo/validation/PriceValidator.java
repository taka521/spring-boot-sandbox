package com.example.validation_demo.validation;

import com.example.validation_demo.domain.Goods;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PriceValidator implements ConstraintValidator<Price, Goods> {

    private int min;
    private int max;

    @Override
    public void initialize(Price annotation) {
        this.min = annotation.min();
        this.max = annotation.max();
    }

    @Override
    public boolean isValid(Goods goods, ConstraintValidatorContext context) {

        if (goods == null) {
            return false;
        }

        int price = goods.getPrice();
        return price >= min && price <= max;
    }

}