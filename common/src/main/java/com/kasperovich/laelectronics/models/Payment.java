package com.kasperovich.laelectronics.models;

import com.kasperovich.laelectronics.enums.PaymentProviders;
import com.kasperovich.laelectronics.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "payments")
@EqualsAndHashCode(exclude = "order")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    Order order;

    @Column(length = 10, precision = 2)
    Long amount;

    @Column(name = "provider")
    @Enumerated(EnumType.STRING)
    PaymentProviders provider;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    PaymentStatus status;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="creationDate", column = @Column(name = "creation_date")),
            @AttributeOverride(name = "modificationDate", column = @Column(name = "modification_date"))
    })
    Edit editData;
}