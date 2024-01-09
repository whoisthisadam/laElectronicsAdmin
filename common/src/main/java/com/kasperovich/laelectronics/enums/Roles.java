package com.kasperovich.laelectronics.enums;

public enum Roles {
    USER(1),
    ADMIN(3),

    MODERATOR(8);

    public int code;

    Roles(int code) {
        this.code=code;
    }
}
