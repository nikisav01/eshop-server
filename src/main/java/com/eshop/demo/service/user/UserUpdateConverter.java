package com.eshop.demo.service.user;

import com.eshop.demo.api.dto.user.UserUpdateDTO;
import com.eshop.demo.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserUpdateConverter {

    public User toModel(UserUpdateDTO userUpdateDTO) {
        return new User(
                userUpdateDTO.getUserID(),
                userUpdateDTO.getUsername(),
                userUpdateDTO.getPassword(),
                userUpdateDTO.getSurname(),
                userUpdateDTO.getName(),
                userUpdateDTO.getAddress(),
                "",
                new ArrayList<>(),
                null,
                new ArrayList<>()
        );
    }

    public UserUpdateDTO fromModel(User user) {
        return new UserUpdateDTO(
                user.getUserID(),
                user.getUsername(),
                user.getPassword(),
                user.getSurname(),
                user.getName(),
                user.getAddress()
        );
    }

    public List<User> toModelMany(Collection<UserUpdateDTO> userUpdateDTOS) {
        return userUpdateDTOS.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    public List<UserUpdateDTO> fromModelMany(Collection<User> users) {
        return users.stream()
                .map(this::fromModel)
                .collect(Collectors.toList());
    }

}
