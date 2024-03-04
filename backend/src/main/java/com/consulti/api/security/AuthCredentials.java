package com.consulti.api.security;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class AuthCredentials {
    private String userName;

    private String password;

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
