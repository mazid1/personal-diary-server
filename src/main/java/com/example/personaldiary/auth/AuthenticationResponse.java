package com.example.personaldiary.auth;

import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticationResponse {
    private final String token;
    private final String username;

    public AuthenticationResponse(String token, UserDetails userDetails) {
        this.token = token;
        this.username = userDetails.getUsername();
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }
}
