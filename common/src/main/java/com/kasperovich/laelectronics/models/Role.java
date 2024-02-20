package com.kasperovich.laelectronics.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kasperovich.laelectronics.enums.Roles;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(exclude = {
        "users"
})
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @Column
    @Enumerated(EnumType.STRING)
    Roles name;

    @OneToMany(mappedBy = "role")
    @JsonIgnore
    Set<User>users;



}
