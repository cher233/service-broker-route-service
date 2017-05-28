package org.servicebroker.routeservice.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

/**
 * Created by Cher on 24/05/2017.
 */


@Data
@Entity
@RequiredArgsConstructor
@Table(name = "filters_to_route", schema="route_service")
public class FilterToRoute
{

    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //private int id;

    //@ManyToOne(fetch=FetchType.LAZY)
    //@JoinColumn(name="route_id")
    //private Route route;

    @EmbeddedId
    private FilterToRouteKey key;

    @Column(name = "app_guid")
    private String appGuid;
}

