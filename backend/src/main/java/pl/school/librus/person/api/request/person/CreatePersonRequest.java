package pl.school.librus.person.api.request.person;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pl.school.librus.person.api.request.address.CreateAddressRequest;

public record CreatePersonRequest(

    @NotBlank(message = "Imię jest wymagane")
    String firstname,

    @NotBlank(message = "Nazwisko jest wymagane")
    String surname,

    @Email(message = "Email jest w niepoprawnym formacie")
    @NotBlank(message = "Email jest wymagany")
    String email,

    String phone,

    @NotNull(message = "Addres jest wymagany")
    CreateAddressRequest address
){}
