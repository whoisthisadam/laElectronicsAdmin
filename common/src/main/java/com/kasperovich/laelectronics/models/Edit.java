package com.kasperovich.laelectronics.models;

import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Data
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Edit {

    Timestamp creationDate;

    Timestamp modificationDate;

}
