package pl.school.librus.person.api.request.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateAddressRequest(

    @NotBlank(message = "Miasto jest wymagane")
    String city,

    @NotBlank(message = "Ulica jest wymagana")
    String street,

    @Pattern(regexp = "^[0-9]{2}-[0-9]{3}$", message = "Kod pocztowy powinien być w formacie xx-xxx")
    String postCode,

    @NotBlank(message = "Numer budynku jest wymagany")
    String buildingNumber,

    String buildingFloor,
    String doorCode
){}
