package pl.school.librus.user.api.response;

public record UserDetailsResponse(

    String id,
    String username,
    String password,
    String email,
    String firstname,
    String surname,
    String phone
) {}
