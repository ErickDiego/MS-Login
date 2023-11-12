package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseSignUpEntity {
    private String id;
    private String created;
    private String lastLogin;
    private String token;
    private boolean isAcive;
}
