package com.examples.service;

import com.examples.entity.Product;
import com.examples.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultProductService implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Iterable<Product> findAllProducts(String filter) {
        if(filter == null || !filter.isBlank()) {
            return this.productRepository.findAllByTitleLikeIgnoreCase("%" + filter + "%");
        }
        return this.productRepository.findAll();
    }

    @Override
    @Transactional
    public Product createProduct(String title, String details) {
        return this.productRepository.save(new Product(null,title,details));
    }

    @Override
    public Optional<Product> findProduct(int productId) {
        return this.productRepository.findById(productId);
    }

    @Override
    public void updateProduct(Integer id, String title, String details) {
        this.productRepository.findById(id).ifPresentOrElse(
                product -> {
                    product.setTitle(title);
                    product.setDetails(details);
                },()->{throw new NoSuchElementException("No product found with id: "+id);}
        );
    }

    @Override
    public void deleteProduct(Integer id) {
        this.productRepository.findById(id).ifPresentOrElse(
                product -> {productRepository.deleteById(id);
                    },
                ()->{throw new NoSuchElementException("No product found with id: "+id);}
        );
    }
}
