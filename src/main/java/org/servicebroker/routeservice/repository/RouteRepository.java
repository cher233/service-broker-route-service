package org.servicebroker.routeservice.repository;

import org.servicebroker.routeservice.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Cher on 28/05/2017.
 */
@Repository
public interface RouteRepository extends JpaRepository<Route,Integer>
{

    Route findFirstByBindingId(String id);
    List<Route> findAllByBindingId(String bindingId);
}
