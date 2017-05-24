package org.servicebroker.routeservice.entity;

import lombok.Data;
import lombok.NonNull;
import org.apache.openjpa.persistence.jdbc.Columns;
import org.servicebroker.routeservice.model.Filters;

import javax.persistence.*;

import java.util.ArrayList;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

/**
 * Created by Cher on 24/05/2017.
 */


@Data
@Embeddable
//@Table(name = "filters_to_route", schema="route_service")
public class FilterToRoute
{

    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //private int id;

    //@ManyToOne(fetch=FetchType.LAZY)
    //@JoinColumn(name="route_id")
    //private Route route;

    @Column(name = "filer_id")
    private Filters filterId;

    @Column(name = "app_guid")
    private String appGuid;
}
