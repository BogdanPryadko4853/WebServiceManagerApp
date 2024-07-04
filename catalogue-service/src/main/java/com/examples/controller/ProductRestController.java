package com.examples.controller;


import com.examples.controller.payload.UpdateProductPayload;
import com.examples.entity.Product;
import com.examples.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.NoSuchElementException;


@RestController
@RequiredArgsConstructor
@RequestMapping("catalogue-api/products/{productId:\\d+}")
public class ProductRestController {

    private final ProductService productService;

    private final MessageSource messageSource;

    @ModelAttribute("product")
    public Product getProduct(@PathVariable("productId") int productId) {
        return this.productService.findProduct(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
    }

    @GetMapping
    public Product findProduct(@ModelAttribute("product") Product product) {
        return product;
    }

    @PatchMapping
    public ResponseEntity<?> updateProduct(@PathVariable("productId") int productId,
                                           @Valid @RequestBody UpdateProductPayload payload,
                                           BindingResult bindingResult) throws BindException {

        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else throw new BindException(bindingResult);
        } else {

            this.productService.updateProduct(productId, payload.title(), payload.description());

            return ResponseEntity.noContent()
                    .build();
        }
    }

    @DeleteMapping
    private ResponseEntity<Void> deleteProduct(
            @PathVariable("productId") int productId) {
        this.productService.deleteProduct(productId);
        return ResponseEntity
                .noContent().build();
    }


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ProblemDetail> handlerNoSuchElementException(NoSuchElementException exception
            , Locale locale) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                        this.messageSource.getMessage(exception.getMessage(), new Object[0],
                                exception.getMessage(), locale)));
    }

}