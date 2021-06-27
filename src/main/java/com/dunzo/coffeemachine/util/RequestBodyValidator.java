package com.dunzo.coffeemachine.util;

import lombok.NonNull;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

public class RequestBodyValidator<T> {

    public Map<String, List<String>> validateRequestBody(T rules) {

        Map<String, List<String>> errorList = new HashMap<>();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(rules);
        violations
                .parallelStream()
                .forEach(
                        (violation) ->
                                addError(
                                        errorList,
                                        String.valueOf(violation.getPropertyPath()),
                                        violation.getMessage()));
        return errorList;
    }

    public Map<String, List<String>> addError(
            Map<String, List<String>> errorList, @NonNull String key, @NonNull String msg) {

        if (errorList.containsKey(key)) {
            List<String> errors = errorList.get(key);
            errors.add(msg);
            errorList.put(key, errorList.get(key));
        } else {
            errorList.put(
                    key,
                    new ArrayList<>() {
                        {
                            add(msg);
                        }
                    });
        }
        return errorList;
    }
}
