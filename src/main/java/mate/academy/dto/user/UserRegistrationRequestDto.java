package mate.academy.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import mate.academy.validation.FieldMatch;
import org.hibernate.validator.constraints.Length;

@FieldMatch(first = "password", second = "repeatPassword", message = "Passwords must match")
public record UserRegistrationRequestDto(@NotBlank @Email String email,
                @NotBlank @Length(min = 8, max = 20) String password,
                @NotBlank String repeatPassword,
                @NotBlank String firstName,
                @NotBlank String lastName,
                String shippingAddress) {

}
