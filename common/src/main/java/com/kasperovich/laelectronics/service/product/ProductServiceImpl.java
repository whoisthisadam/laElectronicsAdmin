package com.kasperovich.laelectronics.service.product;

import com.kasperovich.laelectronics.exception.NotDeletableStatusException;
import com.kasperovich.laelectronics.models.Edit;
import com.kasperovich.laelectronics.models.Product;
import com.kasperovich.laelectronics.enums.ProductStatus;
import com.kasperovich.laelectronics.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    @Override
    public List<Product> findAll() {
        return productRepository.findAll().stream().filter(x->!x.getIsDeleted()).collect(Collectors.toList());
    }

    @Override
    public Product createProduct(@Valid Product product) {
        product.setEditData(new Edit(new Timestamp(new Date().getTime()), null));
        product.setStatus(ProductStatus.values()[new Random().nextInt(3)]);//TODO remove random when release
        return productRepository.save(product);
    }

    @Override
    public Product deleteProduct(Long id) throws NotDeletableStatusException {
        Product product=productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        product.setIsDeleted(true);
        return productRepository.save(product);
//        if(product.getStatus()== ProductStatus.OUT_OF_STOCK){
//            product.setIsDeleted(true);
//            return productRepository.save(product);
//        }
//        else throw new NotDeletableStatusException(
//                "Unable to delete product with status "+product.getStatus().toString()
//        );
    }

    @Override
    public Product updateProduct(@Valid Product product) {
        product.setEditData(
                new Edit(
                        product.getEditData().getCreationDate(), new Timestamp(new Date().getTime())
                )
        );
        return productRepository.save(product);
    }
}
