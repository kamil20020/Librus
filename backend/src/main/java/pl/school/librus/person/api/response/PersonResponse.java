package pl.school.librus.person.api.response;

import pl.school.librus.person.PersonAddress;

public record PersonResponse(

    String id,
    String firstname,
    String surname,
    String email,
    String phone,
    PersonAddress address,
    String userId
){}
