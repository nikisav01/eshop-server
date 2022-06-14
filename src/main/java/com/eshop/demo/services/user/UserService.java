package com.eshop.demo.services.user;

import com.eshop.demo.dao.UserJpaRepository;
import com.eshop.demo.exceptions.EntityNotFound;
import com.eshop.demo.exceptions.EntityStateException;
import com.eshop.demo.model.User;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.Collection;
import java.util.Optional;

@Service
public class UserService implements UserSPI {

    private final UserJpaRepository repository;

    public UserService(UserJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public User create(User user) {
        if (repository.findById(user.getUserID()).isPresent())
            throw new EntityStateException(user);
        repository.save(user);
        return repository.findById(user.getUserID()).get();
    }

    @Override
    public Optional<User> readById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<User> readByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public Collection<User> readAll() {
        return repository.findAll();
    }

    @Override
    public User update(User user) {
        if (repository.findById(user.getUserID()).isEmpty())
            throw new EntityNotFound(user);
        repository.save(user);
        return repository.findById(user.getUserID()).get();
    }

    @Override
    public void deleteById(Long id) {
        if (repository.findById(id).isEmpty())
            throw new EntityNotFound();
        repository.deleteById(id);
    }

    @Override
    public void deleteByUsername(String username) {
        repository.delete(
                repository.findByUsername(username).orElseThrow(EntityNotFound::new)
        );
    }
}
