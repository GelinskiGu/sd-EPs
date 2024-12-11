package com.gelinski.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Account {
    private Long id;

    private String name;

    private String user;

    private String password;

    private String typeUser;
}
