package mate.academy.dto.user;

import org.hibernate.validator.constraints.Length;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginRequestDto(
        @NotBlank @Email String email,
        @NotBlank @Length(min = 8, max = 20) String password) {

}
