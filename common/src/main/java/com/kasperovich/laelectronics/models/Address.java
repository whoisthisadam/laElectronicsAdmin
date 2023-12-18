package com.kasperovich.laelectronics.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(exclude = "users")
@Table(name = "address")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    Long id;

    @Column(name = "line_1")
    String lineOne;

    @Column(name = "line_2")
    String lineTwo;

    @Column(name = "city")
    String city;

    @Column(name = "province")
    String province;

    @Column(name = "postcode")
    String postcode;

    @Column(name = "country")
    String country;

    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    Set<User>users;
}
