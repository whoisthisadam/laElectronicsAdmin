package com.kasperovich.laelectronics.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(exclude = "subscriptions")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "organizations")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Organization{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "name")
    String name;

    @OneToMany(mappedBy = "organization")
    Set<Subscription> subscriptions;

}
