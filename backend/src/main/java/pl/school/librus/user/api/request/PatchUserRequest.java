package pl.school.librus.user.api.request;

public record PatchUserRequest(

    String username,
    String password,
    String email,
    String firstname,
    String surname,
    String phone
){}
