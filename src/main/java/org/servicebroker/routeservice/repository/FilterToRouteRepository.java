package org.servicebroker.routeservice.repository;

import org.servicebroker.routeservice.entity.FilterToRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Cher on 28/05/2017.
 */
@Repository
public interface FilterToRouteRepository extends JpaRepository<FilterToRoute, Integer> {

    List<FilterToRoute> findAllByRoute_RouteId(int id);
    List<FilterToRoute> findAllByRoute_BindingId(String bindingId);
}
