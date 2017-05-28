package org.servicebroker.routeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Cher on 28/05/2017.
 */
public interface ServiceInstanceRepository extends JpaRepository<Object,Long> { //needs to change to Service instanve when created
}
