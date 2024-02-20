package com.kasperovich.laelectronics.models;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "name")
    String name;

    @Column(name = "price")
    Long price;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="creationDate", column = @Column(name = "creation_date")),
            @AttributeOverride(name = "modificationDate", column = @Column(name = "modification_date"))
    })
    Edit editData;

    @Column(name = "is_deleted")
    Boolean isDeleted=false;

    @Column(name = "is_available")
    Boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "discount_id")
    Discount subDiscount;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    Organization organization;

    @OneToMany(mappedBy = "subscription")
    Set<Order> orders;

}
