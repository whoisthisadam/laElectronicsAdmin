package com.kasperovich.laelectronics.service.product;

import com.kasperovich.laelectronics.exception.NotDeletableStatusException;
import com.kasperovich.laelectronics.models.Product;

import java.util.List;

public interface ProductService {

    public List<Product>findAll();

    public Product createProduct(Product product);

    public Product deleteProduct(Long id) throws NotDeletableStatusException;

    public Product updateProduct(Product product);


}
