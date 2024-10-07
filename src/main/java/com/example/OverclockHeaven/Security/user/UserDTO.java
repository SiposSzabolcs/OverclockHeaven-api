package com.example.OverclockHeaven.Security.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private Role role;
}