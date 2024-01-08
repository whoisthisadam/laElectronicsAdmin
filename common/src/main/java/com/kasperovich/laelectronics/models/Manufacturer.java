package com.kasperovich.laelectronics.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(exclude = "products")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "manufacturers")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Manufacturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "name")
    String name;

    @OneToMany(mappedBy = "manufacturer", fetch = FetchType.EAGER)
    Set<Product> products;

}
