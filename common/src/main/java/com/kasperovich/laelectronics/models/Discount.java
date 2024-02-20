package com.kasperovich.laelectronics.models;

import com.kasperovich.laelectronics.enums.Discounts;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(exclude = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "discount")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    Discounts name;

    @Column(name = "discount_percent")
    @Max(100)
    @Positive
    Integer discountPercent;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "creationDate", column = @Column(name = "creation_date")),
            @AttributeOverride(name = "modificationDate", column = @Column(name = "modification_date"))
    })
    Edit editData;

    @Column(name = "is_deleted")
    Boolean isDeleted = false;

    @OneToMany(mappedBy = "userDiscount")
    Set<User> users;

    @OneToMany(mappedBy = "subDiscount")
    Set<Subscription> subscriptions;
}
