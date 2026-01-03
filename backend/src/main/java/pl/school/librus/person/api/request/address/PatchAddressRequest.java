package pl.school.librus.person.api.request.address;

import jakarta.validation.constraints.Pattern;

public record PatchAddressRequest(

    String city,
    String street,

    @Pattern(regexp = "^[0-9]{2}-[0-9]{3}$", message = "Kod pocztowy powinien być w formacie xx-xxx")
    String postCode,

    String buildingNumber,
    String buildingFloor,
    String doorCode
){}
