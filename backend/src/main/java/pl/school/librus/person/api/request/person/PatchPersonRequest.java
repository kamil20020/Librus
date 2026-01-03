package pl.school.librus.person.api.request.person;

import pl.school.librus.person.api.request.address.PatchAddressRequest;

public record PatchPersonRequest(

    String firstname,
    String surname,
    String email,
    String phone,
    PatchAddressRequest address
){}
