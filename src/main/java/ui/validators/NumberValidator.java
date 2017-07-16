package ui.validators;

import com.vaadin.data.Validator;

import java.math.BigInteger;

public class NumberValidator implements Validator {
    @Override
    public void validate(Object value) throws InvalidValueException {
        try {

            if (value != null)// & (value.toString().length() >= 8 & value.toString().length() <= 12) )
                new BigInteger(value.toString());

        } catch (NumberFormatException e) {
            throw new InvalidValueException("The value is not a number");
        }
    }
}