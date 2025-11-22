package pl.school.librus.security.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequest(

    @NotBlank(message = "Nazwa użytkownika jest wymagana")
    String username,

    @NotBlank(message = "Hasło jest wymagane")
    String password
){}
