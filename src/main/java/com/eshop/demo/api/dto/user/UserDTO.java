package com.eshop.demo.api.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class UserDTO {

    private Long userID;

    private String username;

    private String password;

    private String surname;

    private String name;

    private String address;

    private List<String> rolesIDs;

    private Long cartID;

    private List<Long> likedProductsIDs;

}
