package org.servicebroker.routeservice.model;

/**
 * Created by Cher on 24/05/2017.
 */
public enum FiltersType
{
    Defualt(1),
    Directory_Traversal(2),
    Authentication_Bypass(3),
    Sql_Injection(4),
    XSS(5);
    private int value;

    private FiltersType(int value) {
        this.value = value;
    }
}
