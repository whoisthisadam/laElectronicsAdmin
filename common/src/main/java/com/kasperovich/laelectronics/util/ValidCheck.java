package com.kasperovich.laelectronics.util;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@NoArgsConstructor
@Component
public class ValidCheck {


    public boolean isEmailValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public boolean isPasswordValid(String str) {
        char ch;
        boolean flag=false;
        for(int i=0;i < str.length();i++) {
            ch = str.charAt(i);
            if(!Character.isLowerCase(ch)){
                flag=true;
            }
        }
        return flag;
    }
}
