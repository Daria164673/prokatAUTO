package org.voroniuk.prokat.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationService {

    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    public static boolean emailValidator(String email)
        {
        if (email == null) {
            return false;
        }

        return email.matches(EMAIL_REGEX);
    }
}
