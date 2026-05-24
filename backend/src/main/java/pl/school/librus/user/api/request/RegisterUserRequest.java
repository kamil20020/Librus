package pl.school.librus.user.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(

    @NotBlank(message = "Nazwa użytkownika jest wymagana")
    String username,

    @Size(
        message = "Długość hasła powinna wynosić co najmniej 8 znaków",
        min = 8
    )
    @NotBlank(message = "Hasło jest wymagane")
    String password,

    @Email(message = "Podano niepoprawny adres e-mail")
    @NotBlank(message = "Adres e-mail jest wymagany")
    String email,

    String phone
) {}
