package org.servicebroker.routeservice.entity;

import lombok.*;
import javax.persistence.*;

/**
 * Created by Cher on 24/05/2017.
 */


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "filters_to_route", schema="route_service")
public class FilterToRoute{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @Setter
    @OneToOne
    @JoinColumn(name = "filter_id")
    private Filter filter;

    @NonNull
    @Setter
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "route_id")
    private Route route;


    @Column(name = "app_guid", length = 36)
    private String appGuid;

}

