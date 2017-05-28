package org.servicebroker.routeservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Nofar on 23/05/2017.
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "route_info", schema="route_service")
public class Route {

    @Id
    @Column(name = "route_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routeId;

    @NonNull
    @Column(name = "service_id")
    private Long serviceId;

    @NonNull
    @Column(name = "route_name")
    private String routeName;

    //@OneToMany(mappedBy = "route")
    //@ElementCollection
    //@CollectionTable(name = "filters_to_route", joinColumns = @JoinColumn(name = "route_id"))
    //private Set<FilterToRoute> filters = new HashSet<FilterToRoute>();
}
