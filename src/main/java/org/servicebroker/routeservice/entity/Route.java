package org.servicebroker.routeservice.entity;

import lombok.*;

import javax.persistence.*;

/**
 * Created by Nofar on 23/05/2017.
 */

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "route_info", schema="route_service")
public class Route {
    @Id
    @Column(name = "route_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int routeId;

    @NonNull
    @Setter
    @OneToOne
    @JoinColumn(name = "service_id", referencedColumnName = "id")
    private ServiceInstanceEntity service;

    @NonNull
    @Column(name = "route_name", nullable = false, length = 225)
    private String routeName;

    @NonNull
    @Column(name = "binding_id", nullable = false, length = 36)
    private String bindingId;
}
