package org.servicebroker.routeservice.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Nofar on 23/05/2017.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "route_info", schema="route_service")
public class Route {

    @Id
    @Getter
    @Column(name = "route_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routeId;

    @NonNull
    @Column(name = "service_id")
    private Long serviceId;

    @NonNull
    @Column(name = "route_name")
    private String routeName;

    public Route(Long serviceId, String routeName) {
        this.serviceId = serviceId;
        this.routeName = routeName;
    }

    //@OneToMany(mappedBy = "route")
    //@ElementCollection
    //@CollectionTable(name = "filters_to_route", joinColumns = @JoinColumn(name = "route_id"))
    //private Set<FilterToRoute> filters = new HashSet<FilterToRoute>();
}
