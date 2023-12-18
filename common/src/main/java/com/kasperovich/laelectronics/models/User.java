package com.kasperovich.laelectronics.models;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "orders")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "login", column = @Column(name = "user_login")),
            @AttributeOverride(name = "password", column = @Column(name = "user_password"))
    })
    Credentials credentials;

    @Column(name = "first_name")
    @NotBlank
    @Size(max = 20)
    String firstName;

    @Column(name = "last_name")
    @NotBlank
    @Size(max = 30)
    String lastName;

    @Column(name = "mobile_phone")
    @NotBlank
    @Size(max = 15)
    String mobilePhone;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "creationDate", column = @Column(name = "creation_date")),
            @AttributeOverride(name = "modificationDate", column = @Column(name = "modification_date"))
    })
    Edit editData = new Edit(new Timestamp(new Date().getTime()), null);

    @Column(name = "is_deleted")
    Boolean isDeleted = false;

    @Column(name = "email", unique = true)
    @Email
    @Size(max = 100)
    String email;

    @ManyToOne
    @JoinColumn(name = "discount_id")
    Discount userDiscount;

    @ManyToOne
    @JoinColumn(name = "address_id")
    @JsonIgnore
    Address address;

    @ManyToOne
    @JoinColumn(name = "role_id")
    Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    Set<Order> orders;

    public void setPassword(String password){
        this.getCredentials().setPassword(password);
    }
}

