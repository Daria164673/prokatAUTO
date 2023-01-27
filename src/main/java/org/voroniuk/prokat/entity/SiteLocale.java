package org.voroniuk.prokat.entity;

import java.util.Locale;

/**
 * Enum of locales that used in application
 *
 * @author D. Voroniuk
 */
public enum SiteLocale {
    EN(new Locale("en", "US")),
    RU(new Locale("ru", "RU")),
    UA(new Locale("uk", "UA"));

    private Locale locale;

    SiteLocale(Locale l) {
        locale = l;
    }

    public Locale getLocale() {
        return locale;
    }

}
