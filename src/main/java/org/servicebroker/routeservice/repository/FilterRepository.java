package org.servicebroker.routeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.servicebroker.routeservice.entity.FilterEntity;
import org.springframework.stereotype.Repository;

/**
 * Created by Cher on 10/06/2017.
 */

@Repository
public interface FilterRepository extends JpaRepository<FilterEntity,Integer> {

}
