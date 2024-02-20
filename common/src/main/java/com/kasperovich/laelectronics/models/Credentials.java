package com.kasperovich.laelectronics.models;


import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Setter
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Credentials {

    String login;

    String password;

}
