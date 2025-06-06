package vn.utc.service.dtos;

import lombok.AllArgsConstructor;


public class TokenRefreshResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";

    public String getAccessToken() {
        return accessToken;
    }

    public TokenRefreshResponse setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public TokenRefreshResponse setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getTokenType() {
        return tokenType;
    }

    public TokenRefreshResponse setTokenType(String tokenType) {
        this.tokenType = tokenType;
        return this;
    }
}
