package nl.han.fswd.travelexpensepro.security;

public class JwtResponse {
    private String accessToken;
    private String tokenType = "Bearer";

    public JwtResponse(String token, String bearer) {
        this.accessToken = token;
        this.tokenType = bearer;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }
}