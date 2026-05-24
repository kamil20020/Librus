package pl.school.librus.user.api.response;

public record UserDetailsResponse(

    String id,
    String username,
    String email,
    String phone
) {}
