package org.servicebroker.routeservice.entity;

import lombok.Data;
import org.servicebroker.routeservice.model.Filters;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class FilterToRouteKey implements Serializable {
    @Column(name = "filer_id")
    private Filters filterId;

    @Column(name = "route_id")
    private Long routeId;
}
