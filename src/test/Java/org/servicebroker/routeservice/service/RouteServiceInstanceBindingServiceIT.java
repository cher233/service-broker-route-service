package org.servicebroker.routeservice.service;

import org.junit.*;
import org.junit.runner.RunWith;
import org.servicebroker.routeservice.entity.Filter;
import org.servicebroker.routeservice.entity.FilterToRoute;
import org.servicebroker.routeservice.entity.Route;
import org.servicebroker.routeservice.entity.ServiceInstanceEntity;
import org.servicebroker.routeservice.repository.FilterRepository;
import org.servicebroker.routeservice.repository.FilterToRouteRepository;
import org.servicebroker.routeservice.repository.RouteRepository;
import org.servicebroker.routeservice.repository.ServiceInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cher on 12/06/2017.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RouteServiceInstanceBindingServiceIT {

    private static EmbeddedDatabase db;

    @Autowired
    private ServiceInstanceRepository serviceRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private FilterToRouteRepository filterToRouteRepository;

    @Autowired
    private FilterRepository filterRepository;

    @BeforeClass
    public static void setUp() {
        // creates an HSQL in-memory database populated from default scripts
        // classpath:schema.sql and classpath:data.sql
        db = new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .ignoreFailedDrops(true)
                .addScript("db/migration/V1__Initial_Setup.sql")
                .build();

    }

   private void enterServiceInstance() {
        serviceRepository.save(ServiceInstanceEntity.builder().
                serviceId("1").
                planId("plan").
                organizationGuid("org").
                spaceGuid("space").
                build());
    }

    @Test
    public void testASaveRoute()
    {
        deleteAllRoutes();
        enterServiceInstance();
        ServiceInstanceEntity service = serviceRepository.findFirstByServiceId("1");
        Route routeToSave = Route.builder().
                routeName("https:\\sdfdsf").
                service(service).
                bindingId("1").
                build();
        Assert.assertEquals(routeToSave,routeRepository.save(routeToSave));
    }

    @Test
    public void testBSaveFilterToRouteList()
    {
        List<FilterToRoute> filterToRouteList = new ArrayList<>();
        Route routeForeignKey = routeRepository.findFirstByBindingId("1");
        Filter filterForeignKey = filterRepository.getOne(1);
        filterToRouteList.add(FilterToRoute.builder().route(routeForeignKey).filter(filterForeignKey).build());
        filterForeignKey = filterRepository.getOne(2);
        filterToRouteList.add(FilterToRoute.builder().route(routeForeignKey).filter(filterForeignKey).build());
        Assert.assertEquals(filterToRouteList,filterToRouteRepository.save(filterToRouteList));
    }

    @Test
    public void testCFindFilterToRouteList()
    {
        List<FilterToRoute> filterToRouteList = filterToRouteRepository.findAllByRoute_BindingId("1");
        Assert.assertNotNull(filterToRouteList);
        int routeId = routeRepository.findFirstByBindingId("1").getRouteId();
        filterToRouteList = filterToRouteRepository.findAllByRoute_RouteId(routeId);
        Assert.assertNotNull(filterToRouteList);
    }

    @Test
    public void testDeleteRoute() {
        deleteFilterToRoute();
        Assert.assertEquals(new ArrayList<FilterToRoute>(),filterToRouteRepository.findAllByRoute_BindingId("1"));
        Route routeToDelete = routeRepository.findFirstByBindingId("1");
        routeRepository.delete(routeToDelete);
        Assert.assertNull(routeRepository.findFirstByBindingId("1"));
    }

    private void deleteAllRoutes() {
        List<ServiceInstanceEntity> entityList = serviceRepository.findAll();
        List<Route> routeList = routeRepository.findAll();
        routeRepository.delete(routeList);
        serviceRepository.delete(entityList);
    }

    private void deleteFilterToRoute() {
        List<FilterToRoute> filterToRouteList  = filterToRouteRepository.findAllByRoute_BindingId("1");
        filterToRouteRepository.delete(filterToRouteList);
    }


    @AfterClass
    public static void tearDown() {
        db.shutdown();
    }
}
