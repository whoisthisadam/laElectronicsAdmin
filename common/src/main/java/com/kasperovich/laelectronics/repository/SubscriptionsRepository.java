package com.kasperovich.laelectronics.repository;

import com.kasperovich.laelectronics.models.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionsRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findProductByIdAndIsDeleted(Long id, Boolean isDeleted);

}
