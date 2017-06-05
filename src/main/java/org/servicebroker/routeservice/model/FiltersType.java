package org.servicebroker.routeservice.model;

import lombok.Getter;

/**
 * Created by Cher on 24/05/2017.
 */
public enum FiltersType
{
    DEFAULT(1),
    DIRECTORY_TRAVERSAL(2),
    AUTHORIZATION_BYPASS(3),
    SQL_INJECTION(4),
    XSS(5);

    @Getter
    private int value;

    private FiltersType(int value) {
        this.value = value;
    }

    public static boolean contains(String s)
    {
        s = s.toUpperCase();
        for(FiltersType filters:values())
            if (filters.name().equals(s))
                return true;
        return false;
    }
}
