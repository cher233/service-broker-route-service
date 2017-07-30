package org.servicebroker.routeservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by Cher on 10/06/2017.
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "filter_info", schema="route_service")
public class FilterEntity {
        @Id
        @Column(name = "filter_id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int filterId;

        @Column(name = "filter_name", nullable = false, length = 100)
        private String filerName;
}
