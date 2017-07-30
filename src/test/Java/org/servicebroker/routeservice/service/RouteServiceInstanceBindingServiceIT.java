package org.servicebroker.routeservice.service;

import org.junit.*;
import org.junit.runner.RunWith;
/*import org.cher.entities.FilterEntity;
import org.cher.entities.FilterToRoute;
import org.cher.entities.Route;
import org.cher.entities.ServiceInstanceEntity;*/
import org.servicebroker.routeservice.entity.FilterEntity;
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

    private  EmbeddedDatabase db;

    @Autowired
    private ServiceInstanceRepository serviceRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private FilterToRouteRepository filterToRouteRepository;

    @Autowired
    private FilterRepository filterRepository;

    @Before
    public void setUp() {
        db = new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .ignoreFailedDrops(true)
                .addScript("db/migration/V1__Initial_Setup.sql")
                .build();

    }

    @Test
    public void testSaveRoute() {
        enterServiceInstance();
        ServiceInstanceEntity service = serviceRepository.findFirstByServiceId("1");
        enterRoute(service);
        Assert.assertNotNull(routeRepository.findFirstByBindingId("1"));
    }

    @Test
    public void testSaveFilterToRouteList() {
        // setup
        enterServiceInstance();
        ServiceInstanceEntity service = serviceRepository.findFirstByServiceId("1");
        enterRoute(service);
        // test
        Route routeForeignKey = routeRepository.findFirstByBindingId("1");
        enterFilterToRouteList(routeForeignKey);
        Assert.assertNotNull(filterToRouteRepository.findAllByRoute_BindingId("1"));
    }

    @Test
    public void testDeleteFilterToRoute() {
        //setup
        enterServiceInstance();
        ServiceInstanceEntity service = serviceRepository.findFirstByServiceId("1");
        enterRoute(service);
        // test
        Route route = routeRepository.findFirstByBindingId("1");
        enterFilterToRouteList(route);
        deleteFilterToRoute();
        Assert.assertEquals(new ArrayList<FilterToRoute>(),filterToRouteRepository.findAllByRoute_BindingId("1"));

    }

    @Test
    public  void testDeleteRout() {
        //setup
        enterServiceInstance();
        ServiceInstanceEntity service = serviceRepository.findFirstByServiceId("1");
        enterRoute(service);
        // test
        Route routeToDelete = routeRepository.findFirstByBindingId("1");
        routeRepository.delete(routeToDelete);
        Assert.assertNull(routeRepository.findFirstByBindingId("1"));
    }

    private void enterServiceInstance() {
        serviceRepository.save(ServiceInstanceEntity.builder().
                serviceId("1").
                planId("plan").
                organizationGuid("org").
                spaceGuid("space").
                build());
    }

    private  void enterRoute(ServiceInstanceEntity service)
    {
        Route routeToSave = Route.builder().
                routeName("https:\\sdfdsf").
                service(service).
                bindingId("1").
                build();
        routeRepository.save(routeToSave);
    }

    private void enterFilterToRouteList(Route route) {
        List<FilterToRoute> filterToRouteList = new ArrayList<>();
        FilterEntity filterForeignKey = filterRepository.getOne(1);
        filterToRouteList.add(FilterToRoute.builder().route(route).filter(filterForeignKey).build());
        filterForeignKey = filterRepository.getOne(2);
        filterToRouteList.add(FilterToRoute.builder().route(route).filter(filterForeignKey).build());
        filterToRouteRepository.save(filterToRouteList);
    }

    private void deleteAllRoutesAndServices() {
        List<ServiceInstanceEntity> entityList = serviceRepository.findAll();
        List<Route> routeList = routeRepository.findAll();
        routeRepository.delete(routeList);
        serviceRepository.delete(entityList);
    }

    private void deleteFilterToRoute() {
        List<FilterToRoute> filterToRouteList  = filterToRouteRepository.findAllByRoute_BindingId("1");
        filterToRouteRepository.delete(filterToRouteList);
    }


    @After
    public void tearDown() {
        deleteFilterToRoute();
        deleteAllRoutesAndServices();
        db.shutdown();
    }
}
