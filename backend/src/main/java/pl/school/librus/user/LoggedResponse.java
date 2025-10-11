package pl.school.librus.user;

public record LoggedResponse(

    String accessToken,
    String refreshToken
){}
