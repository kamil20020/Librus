package pl.school.librus.person.api.request.person;

public record SearchPersonRequest(

    String searchText,
    String firstname,
    String surname,
    String phone,
    String city
){}
