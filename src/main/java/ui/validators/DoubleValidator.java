package ui.validators;

import com.vaadin.data.Validator;

public class DoubleValidator implements Validator {
    @Override
    public void validate(Object value) throws InvalidValueException {
        try {

            if (value != null)// & (value.toString().length() >= 8 & value.toString().length() <= 12) )//& value.toString().length() != 0
                new Double(value.toString());

        } catch (NumberFormatException e) {
            throw new InvalidValueException("The value is not a number");
        }
    }
}