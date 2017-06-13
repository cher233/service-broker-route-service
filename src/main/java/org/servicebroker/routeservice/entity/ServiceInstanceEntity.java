package org.servicebroker.routeservice.entity;

import lombok.*;

import javax.persistence.*;

/**
 * Created by Nofar on 29/05/2017.
 */

@Getter
@ToString
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "service_instances", schema="route_service")
public class ServiceInstanceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @Column(name = "service_id", nullable = false, length = 36)
    private String serviceId;

    @NonNull
    @Column(name = "plan_id",nullable = false, length = 36)
    private String planId;

    @NonNull
    @Column(name = "organization_guid", nullable = false, length = 36)
    private String organizationGuid;

    @NonNull
    @Column(name = "space_guid", nullable = false, length = 36)
    private String spaceGuid;

}