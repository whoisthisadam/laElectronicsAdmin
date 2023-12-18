package com.kasperovich.laelectronics.api.dto.users;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteUserDto {

    public enum DeletedStatus{
        DELETED,

        ERROR
    }

    DeletedStatus status;

    Long id;

}
