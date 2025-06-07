package vn.utc.service.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String refreshToken;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private List<String> roles;

    public JwtResponse setToken(String token) {
        this.token = token;
        return this;
    }

    public JwtResponse setType(String type) {
        this.type = type;
        return this;
    }

    public JwtResponse setId(Long id) {
        this.id = id;
        return this;
    }

    public JwtResponse setUsername(String username) {
        this.username = username;
        return this;
    }

    public JwtResponse setEmail(String email) {
        this.email = email;
        return this;
    }

    public JwtResponse setRoles(List<String> roles) {
        this.roles = roles;
        return this;
    }

    public JwtResponse setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public JwtResponse setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public JwtResponse setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }
}
