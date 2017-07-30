package org.servicebroker.routeservice.service;

import org.junit.*;
import org.junit.runner.RunWith;
//import org.cher.entities.ServiceInstanceEntity;
import org.servicebroker.routeservice.entity.ServiceInstanceEntity;
import org.servicebroker.routeservice.repository.ServiceInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

/**
 * Created by Nofar on 06/06/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RouteServiceInstanceServiceIT {

    private static EmbeddedDatabase db;

    @Autowired
    private ServiceInstanceRepository serviceRepository;


    @BeforeClass
    public static void setUp() {
        db = new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(H2)
                .setScriptEncoding("UTF-8")
                .ignoreFailedDrops(true)
                .addScript("db/migration/V1__Initial_Setup.sql")
                .build();
    }


    @Test
    public void testSave() {
        ServiceInstanceEntity entity = ServiceInstanceEntity.builder().
                serviceId("1").
                organizationGuid("org").
                spaceGuid("space").
                planId("plan").
                build();
        ServiceInstanceEntity savedEntity =  serviceRepository.save(entity);
        Assert.assertEquals(savedEntity,entity);
    }


    @Test
    public void testDataAccess() {
        ServiceInstanceEntity entity = serviceRepository.findFirstByServiceId("1");
        Assert.assertNotNull(entity);
    }

    @Test
    public void testDelete() {
        ServiceInstanceEntity entity = serviceRepository.findFirstByServiceId("1");
        serviceRepository.delete(entity);
    }

    @AfterClass
    public static void tearDown() {
        db.shutdown();
    }
}
