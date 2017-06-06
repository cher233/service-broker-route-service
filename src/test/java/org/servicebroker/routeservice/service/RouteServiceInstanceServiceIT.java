package org.servicebroker.routeservice.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.servicebroker.routeservice.repository.ServiceInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

/**
 * Created by Nofar on 06/06/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class RouteServiceInstanceServiceIT {

    private EmbeddedDatabase db;

    @Autowired
    private ServiceInstanceRepository serviceRepository;

    @Before
    public void setUp() {
        // creates an HSQL in-memory database populated from default scripts
        // classpath:schema.sql and classpath:data.sql
        db = new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(H2)
                .setScriptEncoding("UTF-8")
                .ignoreFailedDrops(true)
                .addScript("schema.sql")
                .build();
    }

    @Test
    public void testDataAccess() {

    }

    @After
    public void tearDown() {
        db.shutdown();
    }
}
