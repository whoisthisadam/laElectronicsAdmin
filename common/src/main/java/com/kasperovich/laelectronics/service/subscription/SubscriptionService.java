package com.kasperovich.laelectronics.service.subscription;

import com.kasperovich.laelectronics.exception.NotDeletableStatusException;
import com.kasperovich.laelectronics.models.Subscription;

import java.util.List;

public interface SubscriptionService {

    public List<Subscription>findAll();

    public Subscription createProduct(Subscription product);

    public Subscription deleteProduct(Long id) throws NotDeletableStatusException;

    public Subscription updateProduct(Subscription product);


}
