package com.eshop.demo.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateDTO {

    private Long userID;

    private String username;

    private String password;

    private String surname;

    private String name;

    private String address;

}
