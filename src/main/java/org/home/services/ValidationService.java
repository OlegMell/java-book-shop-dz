package org.home.services;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.naming.Binding;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ValidationService {
    public Map<String, String> getErrors(BindingResult validationResult) {
        return validationResult.getFieldErrors()
                .stream()
                .collect(Collectors
                        .toMap(fieldError ->
                                fieldError.getField() + "Error", FieldError::getDefaultMessage));
    }
}
