package org.servicebroker.routeservice.repository;

import org.servicebroker.routeservice.entity.Filter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Cher on 10/06/2017.
 */

@Repository
public interface FilterRepository extends JpaRepository<Filter,Integer> {
}
