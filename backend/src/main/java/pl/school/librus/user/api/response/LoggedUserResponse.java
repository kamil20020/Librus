package pl.school.librus.user.api.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record LoggedUserResponse(

    String id,
    String username,
    String password,
    String email,
    String firstname,
    String surname,
    String phone
) {}
