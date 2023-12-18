package com.kasperovich.laelectronics.api.controller.exceptionHandler;

import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorContainer {

    private String exceptionId;

    private String errorMessage;

    private int errorCode;

    private String e;
}
