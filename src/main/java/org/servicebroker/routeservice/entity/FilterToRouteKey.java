package org.servicebroker.routeservice.entity;

import lombok.Data;
import org.servicebroker.routeservice.model.FiltersType;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class FilterToRouteKey implements Serializable {
    @Column(name = "filer_id")
    private int filterId;

    @Column(name = "route_id")
    private Long routeId;
}
