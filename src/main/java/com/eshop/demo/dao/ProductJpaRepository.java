package com.eshop.demo.dao;

import com.eshop.demo.model.Category;
import com.eshop.demo.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductJpaRepository extends PagingAndSortingRepository<Product, Long> {

    Page<Product> findProductsByCategories(Category category, Pageable pageable);

}
