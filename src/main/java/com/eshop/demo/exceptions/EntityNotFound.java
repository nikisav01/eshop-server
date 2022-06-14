package com.eshop.demo.exceptions;

public class EntityNotFound extends RuntimeException{

    public EntityNotFound() {
        super("Entity not found.");
    }

    public <E> EntityNotFound(E entity) {
        super("Entity " + entity + " not found.");
    }

}
