package com.eshop.demo.service.user;

import com.eshop.demo.dao.UserJpaRepository;
import com.eshop.demo.exceptions.EntityNotFound;
import com.eshop.demo.exceptions.EntityStateException;
import com.eshop.demo.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserJpaRepository repository;

    User user1 = new User(1L, "username1",
            "pass",
            "Smith",
            "Will",
            "USA",
            "",
            Collections.emptyList(),
            null,
            Collections.emptyList());

    User user2 = new User(2L, "username2",
            "pass",
            "Reynolds",
            "Ryan",
            "USA",
            "",
            Collections.emptyList(),
            null,
            Collections.emptyList());

    User user3 = new User(3L, "username2", // existing username
            "pass",
            "Robbie",
            "Margot",
            "USA",
            "",
            Collections.emptyList(),
            null,
            Collections.emptyList());

    @BeforeEach
    void setUp() {
        Mockito.when(repository.findById(user1.getUserID())).thenReturn(Optional.of(user1));
        Mockito.when(repository.findById(user2.getUserID())).thenReturn(Optional.of(user2));
        Mockito.when(repository.findById(user3.getUserID())).thenReturn(Optional.of(user3));
    }

    @Test
    void create() {
        userService.create(user1);
        Assertions.assertEquals(Optional.of(user1), repository.findById(user1.getUserID()));
        Mockito.verify(repository, Mockito.times(1)).save(user1);

        userService.create(user2);
        Assertions.assertEquals(Optional.of(user2), repository.findById(user2.getUserID()));
        Mockito.verify(repository, Mockito.times(1)).save(user2);

        Mockito.when(repository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        Mockito.when(repository.findByUsername(user2.getUsername())).thenReturn(Optional.of(user2));

        Assertions.assertThrows(EntityStateException.class, () -> userService.create(user3));
        Mockito.verify(repository, Mockito.times(0)).save(user3);
    }

    @Test
    void update() {
        Mockito.when(userService.readByUsername(user2.getUsername())).thenReturn(Optional.of(user2));
        userService.update(user3.getUsername(), user3);
        Mockito.verify(repository, Mockito.times(1)).save(user2);

        Assertions.assertThrows(EntityNotFound.class, () -> userService.update(user1.getUsername(), user1));
    }

    @Test
    void deleteById() {
        userService.deleteById(user2.getUserID());
        Mockito.verify(repository, Mockito.times(1)).deleteById(user2.getUserID());

        Assertions.assertThrows(EntityNotFound.class, () -> userService.deleteById(10L));
        Mockito.verify(repository, Mockito.times(0)).deleteById(10L);
    }

    @Test
    void deleteByUsername() {
        Mockito.when(repository.findByUsername(user2.getUsername())).thenReturn(Optional.of(user2));
        userService.deleteByUsername(user2.getUsername());

        Assertions.assertThrows(EntityNotFound.class, () -> userService.deleteByUsername("abc"));
    }
}