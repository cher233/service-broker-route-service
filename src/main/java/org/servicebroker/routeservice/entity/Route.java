package org.servicebroker.routeservice.entity;

import lombok.*;
import org.hibernate.annotations.Cascade;

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
    //@Column(name = "service_id", nullable = false)
    //private Long serviceId;
    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "service_id")
    private ServiceInstanceEntity service;

    @NonNull
    @Column(name = "route_name", nullable = false, length = 225)
    private String routeName;

    @NonNull
    @Column(name = "binding_id", nullable = false, length = 225)
    private String bindingId;


    //@OneToMany(mappedBy = "route")
    //@ElementCollection
    //@CollectionTable(name = "filters_to_route", joinColumns = @JoinColumn(name = "route_id"))
    //private Set<FilterToRoute> filters = new HashSet<FilterToRoute>();
}
