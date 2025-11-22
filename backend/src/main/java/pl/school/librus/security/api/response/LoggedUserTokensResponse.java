package pl.school.librus.security.api.response;

public record LoggedUserTokensResponse(

    String accessToken,
    String refreshToken
){}
