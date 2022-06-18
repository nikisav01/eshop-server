package com.eshop.demo.service.role;

import com.eshop.demo.model.Role;
import java.util.Collection;
import java.util.Optional;

public interface RoleSPI {

    Role create(Role role);

    Optional<Role> readById(String roleName);

    Collection<Role> readAll();

    Role update(Role role);

    void deleteById(String roleName);

}
