package org.servicebroker.routeservice.repository;

import org.servicebroker.routeservice.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Cher on 28/05/2017.
 */
public interface RouteRepository extends JpaRepository<Route,Long>
{

}
