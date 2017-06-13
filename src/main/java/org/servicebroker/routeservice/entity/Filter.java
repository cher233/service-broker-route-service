package org.servicebroker.routeservice.entity;

/**
 * Created by Nofar on 13/06/2017.
 */


import lombok.*;
import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "filter_info", schema="route_service")
public class Filter {
    @Id
    @Column(name = "filter_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int filterId;

    @Column(name = "filter_name", nullable = false, length = 100)
    private String filerName;
}