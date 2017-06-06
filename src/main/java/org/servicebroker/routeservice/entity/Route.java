package org.servicebroker.routeservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

/**
 * Created by Nofar on 23/05/2017.
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "route_info", schema="route_service")
public class Route {
    //@SequenceGenerator(name="routeGen", initialValue=2,schema = "route_service",allocationSize=1)
    @Id
    @Column(name = "route_id")
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "routeGen")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int routeId;

    @NonNull
    //@Column(name = "service_id", nullable = false)
    //private Long serviceId;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "service_id", referencedColumnName = "id")
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
