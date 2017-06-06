package org.servicebroker.routeservice.entity;

import lombok.*;

import javax.persistence.*;

/**
 * Created by Nofar on 29/05/2017.
 */

@Data
@Entity
@RequiredArgsConstructor
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "service_instances", schema="route_service")
public class ServiceInstanceEntity {
    //@SequenceGenerator(name="serviceGen", initialValue=3, schema = "route_service",allocationSize=1)
    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "serviceGen")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @Column(name = "service_id", nullable = false, length = 225)
    private String serviceId;

    @NonNull
    @Column(name="plan_id",nullable = false, length = 225)
    private String plan;

    @NonNull
    @Column(name = "organization_guid", nullable = false, length = 225)
    private String organizationGuid;

    @NonNull
    @Column(name = "space_guid", nullable = false, length = 225)
    private String spaceGuid;

}