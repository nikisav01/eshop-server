package com.eshop.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productID;

    private String productName;

    private String pathToPhotos;

    @ManyToMany(mappedBy = "products")
    private List<Category> categories;

    private Integer price;

    private Integer quantity;

    @OneToMany(mappedBy = "product")
    private List<Image> images;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<CartItem> cartItem;

    @ManyToMany(mappedBy = "likedProducts")
    private List<User> usersSaved;

}
