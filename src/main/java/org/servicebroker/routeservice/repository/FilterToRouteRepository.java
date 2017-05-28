package org.servicebroker.routeservice.repository;

import org.servicebroker.routeservice.entity.FilterToRoute;
import org.servicebroker.routeservice.entity.FilterToRouteKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Cher on 28/05/2017.
 */
public interface FilterToRouteRepository extends JpaRepository<FilterToRoute, FilterToRouteKey> {
}
