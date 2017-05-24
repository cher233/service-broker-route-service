package org.servicebroker.routeservice.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by Nofar on 23/05/2017.
 */
@Data
@Entity
@Table(name = "route_info")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int route_id;
    private int service_id;
    private String routeName;
}
