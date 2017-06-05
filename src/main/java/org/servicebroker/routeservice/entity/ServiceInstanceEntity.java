package org.servicebroker.routeservice.entity;

import lombok.*;
import org.servicebroker.routeservice.model.ServiceInstance;


import javax.annotation.sql.DataSourceDefinition;
import javax.persistence.*;

/**
 * Created by Nofar on 29/05/2017.
 */

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "service_instances", schema="route_service")
public class ServiceInstanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "service_id", nullable = false, length = 225)
    private String serviceId;

    @NonNull
    @Column(nullable = true, length = 225)
    private String plan;

    @NonNull
    @Column(name = "organization_guid", nullable = false, length = 225)
    private String organizationGuid;

    @NonNull
    @Column(name = "space_guid", nullable = false, length = 225)
    private String spaceGuid;

}