package org.servicebroker.routeservice.repository;

import org.servicebroker.routeservice.entity.ServiceInstanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Cher on 28/05/2017.
 */

@Repository
public interface ServiceInstanceRepository extends JpaRepository<ServiceInstanceEntity, Integer> {
     ServiceInstanceEntity findFirstByServiceId(String id);
}
