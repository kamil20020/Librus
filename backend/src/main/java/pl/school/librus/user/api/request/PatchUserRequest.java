package pl.school.librus.user.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PatchUserRequest(

    String username,
    String password,
    String email,
    String firstname,
    String surname,
    String phone
){}
