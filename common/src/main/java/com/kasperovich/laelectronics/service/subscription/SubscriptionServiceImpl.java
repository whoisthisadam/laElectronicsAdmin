package com.kasperovich.laelectronics.service.subscription;

import com.kasperovich.laelectronics.exception.NotDeletableStatusException;
import com.kasperovich.laelectronics.models.Edit;
import com.kasperovich.laelectronics.models.Subscription;
import com.kasperovich.laelectronics.repository.SubscriptionsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionsRepository subscriptionsRepository;
    @Override
    public List<Subscription> findAll() {
        List<Subscription> products = subscriptionsRepository.findAll();
        return products;
    }


    @Override
    public Subscription createProduct(@Valid Subscription product) {
        product.setEditData(new Edit(new Timestamp(new Date().getTime()), null));
        return subscriptionsRepository.save(product);
    }

    @Override
    public Subscription deleteProduct(Long id) throws NotDeletableStatusException {
        Subscription product= subscriptionsRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        product.setIsDeleted(true);
        return subscriptionsRepository.save(product);
    }

    @Override
    public Subscription updateProduct(@Valid Subscription product) {
        product.setEditData(
                new Edit(
                        product.getEditData().getCreationDate(), new Timestamp(new Date().getTime())
                )
        );
        return subscriptionsRepository.save(product);
    }
}
