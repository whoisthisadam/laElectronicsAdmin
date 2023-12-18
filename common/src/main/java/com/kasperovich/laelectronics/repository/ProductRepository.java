package com.kasperovich.laelectronics.repository;

import com.kasperovich.laelectronics.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findProductByIdAndIsDeleted(Long id, Boolean isDeleted);

}
