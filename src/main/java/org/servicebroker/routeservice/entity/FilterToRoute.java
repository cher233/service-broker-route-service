package org.servicebroker.routeservice.entity;

import lombok.*;
import org.servicebroker.routeservice.model.FiltersType;

import javax.persistence.*;

/**
 * Created by Cher on 24/05/2017.
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "filters_to_route", schema="route_service")
public class FilterToRoute{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "filter_id", nullable = false)
    private FiltersType filter;

    //@NonNull
    //@Column(name = "route_id", nullable = false)
    //private Long routeId;

    @NonNull
    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "route_id")
    private Route route;


    @Column(name = "app_guid", length = 225)
    private String appGuid;
}

