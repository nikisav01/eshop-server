package com.eshop.demo.model;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity(name = "entity_user")
@Table(name = "entity_user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address; // address should be a class instead of attribute

    private String pathToPhoto;

    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_name")}
    )
    private List<Role> roles;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cart cart;

    @ManyToMany
    @JoinTable(
            name = "user_liked_product",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "product_user")}
    )
    private List<Product> likedProducts;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!userID.equals(user.userID)) return false;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        int result = userID.hashCode();
        result = 31 * result + username.hashCode();
        return result;
    }
}