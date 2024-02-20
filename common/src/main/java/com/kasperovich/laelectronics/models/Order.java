package com.kasperovich.laelectronics.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "l_users_subscriptions")
@EqualsAndHashCode(exclude = {"payment", "user", "subscription"})
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "sub_id")
    Subscription subscription;

    @OneToOne(mappedBy = "order")
    Payment payment;
}
