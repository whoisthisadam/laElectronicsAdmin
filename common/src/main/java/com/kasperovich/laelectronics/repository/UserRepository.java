package com.kasperovich.laelectronics.repository;

import com.kasperovich.laelectronics.models.Discount;
import com.kasperovich.laelectronics.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByCredentialsLoginAndIsDeleted(String login, Boolean isDeleted);

    List<User>findUsersByUserDiscount(Discount discount);

    Optional<User>findUserById(Long id);

    Optional<User> findUserByEmail(String email);

}
