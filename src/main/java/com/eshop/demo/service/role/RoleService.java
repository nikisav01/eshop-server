package com.eshop.demo.service.role;

import com.eshop.demo.dao.RoleJpaRepository;
import com.eshop.demo.exceptions.EntityNotFound;
import com.eshop.demo.exceptions.EntityStateException;
import com.eshop.demo.model.Role;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service("RoleService")
public class RoleService implements RoleSPI{

    private final RoleJpaRepository repository;

    public RoleService(RoleJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Role create(Role role) {
        if (repository.findById(role.getRoleName()).isPresent())
            throw new EntityStateException(role);
        repository.save(role);
        return repository.findById(role.getRoleName()).get();
    }

    @Override
    public Optional<Role> readById(String roleName) {
        return repository.findById(roleName);
    }

    @Override
    public Collection<Role> readAll() {
        return repository.findAll();
    }

    @Override
    public Role update(Role role) {
        if (repository.findById(role.getRoleName()).isEmpty())
            throw new EntityNotFound(role);
        repository.save(role);
        return repository.findById(role.getRoleName()).get();
    }

    @Override
    public void deleteById(String roleName) {
        if (repository.findById(roleName).isEmpty())
            throw new EntityNotFound();
        repository.deleteById(roleName);
    }
}
