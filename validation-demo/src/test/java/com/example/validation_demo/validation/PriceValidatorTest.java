package com.example.validation_demo.validation;

import com.example.validation_demo.domain.Goods;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class PriceValidatorTest {

    private class TestBean {
        @Price(message = "価格ダメ!!")
        public Goods goods;
    }

    @Test
    public void test_priceValidation() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        TestBean testBean = new TestBean();
        testBean.goods = new Goods("はじめてのBeanValidation", 10_000);

        Set<ConstraintViolation<TestBean>> result = validator.validate(testBean);

        assertThat(result.size(), is(1));
        result.stream().map(ConstraintViolation::getMessage).forEach(System.out::println);
    }

}