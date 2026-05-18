package mate.academy.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class IsbnValidator implements ConstraintValidator<Isbn, String> {

    private static final String PATTERN_OF_ISBN = "\\d{13}";

    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext context) {

        return isbn == null || Pattern.compile(PATTERN_OF_ISBN).matcher(isbn).matches();
    }

}
