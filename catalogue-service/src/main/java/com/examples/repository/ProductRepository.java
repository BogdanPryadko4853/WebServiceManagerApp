package com.examples.repository;


import com.examples.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Iterable<Product> findAllByTitleLikeIgnoreCase(String s);
}
