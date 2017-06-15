package org.servicebroker.routeservice.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.servicebroker.routeservice.entity.Filter;
import org.servicebroker.routeservice.entity.FilterToRoute;
import org.servicebroker.routeservice.entity.Route;
import org.servicebroker.routeservice.entity.ServiceInstanceEntity;
import org.servicebroker.routeservice.repository.FilterRepository;
import org.servicebroker.routeservice.repository.FilterToRouteRepository;
import org.servicebroker.routeservice.repository.RouteRepository;
import org.servicebroker.routeservice.repository.ServiceInstanceRepository;
import org.springframework.cloud.servicebroker.exception.*;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceRouteBindingResponse;
import org.springframework.cloud.servicebroker.model.DeleteServiceInstanceBindingRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Cher on 10/06/2017.
 */
public class RouteServiceInstanceBindingServiceTest {

    private RouteServiceInstanceBindingService bindingService;
    private RouteRepository routeRepository;
    private ServiceInstanceRepository serviceInstanceRepository;
    private FilterToRouteRepository filterToRouteRepository;
    private FilterRepository filterRepository;
    private CreateServiceInstanceBindingRequest createRequest;
    private DeleteServiceInstanceBindingRequest deleteRequest;

    @Before
    public void init(){
        serviceInstanceRepository = mock(ServiceInstanceRepository.class);
        routeRepository = mock(RouteRepository.class);
        filterToRouteRepository = mock(FilterToRouteRepository.class);
        filterRepository = mock(FilterRepository.class);
        bindingService = new RouteServiceInstanceBindingService();
        bindingService.setServiceRepository(serviceInstanceRepository);
        bindingService.setRouteRepository(routeRepository);
        bindingService.setFilterToRouteRepositoryRepository(filterToRouteRepository);
        bindingService.setFilterRepository(filterRepository);
        createRequest = mock(CreateServiceInstanceBindingRequest.class);
        deleteRequest = mock(DeleteServiceInstanceBindingRequest.class);
    }

    @Test(expected = ServiceInstanceDoesNotExistException.class)
    public void createServiceInstanceBindingWithServiceInstanceDoesNotExistsExceptionTest(){
        when(createRequest.getServiceInstanceId()).thenReturn("123");
        when(serviceInstanceRepository.findFirstByServiceId(any())).thenReturn(null);
        bindingService.createServiceInstanceBinding(createRequest);
    }

    @Test(expected = ServiceInstanceBindingExistsException.class)
    public void createServiceInstanceBindingWithBindingIdExistsExceptionTest(){
        when(createRequest.getServiceInstanceId()).thenReturn("123");
        when(createRequest.getBindingId()).thenReturn("bind");
        when(serviceInstanceRepository.findFirstByServiceId(any())).thenReturn(mock(ServiceInstanceEntity.class));
        when(routeRepository.findFirstByBindingId(any())).thenReturn(mock(Route.class));
        bindingService.createServiceInstanceBinding(createRequest);
    }

    @Test(expected = ServiceBrokerInvalidParametersException.class)
    public void createServiceInstanceBindingWithNoRouteExceptionTest(){
        when(createRequest.getServiceInstanceId()).thenReturn("123");
        when(createRequest.getBindingId()).thenReturn("bind");
        when(createRequest.getBoundRoute()).thenReturn(null);
        when(serviceInstanceRepository.findFirstByServiceId(any())).thenReturn(mock(ServiceInstanceEntity.class));
        when(routeRepository.findFirstByBindingId(any())).thenReturn(null);
        bindingService.createServiceInstanceBinding(createRequest);
    }

    @Test(expected = ServiceBrokerInvalidParametersException.class)
    public void createServiceInstanceBindingWithWrongParametersExceptionTest(){
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("filter","a");
        when(createRequest.getServiceInstanceId()).thenReturn("123");
        when(serviceInstanceRepository.findFirstByServiceId(any())).thenReturn(mock(ServiceInstanceEntity.class));
        when(createRequest.getBindingId()).thenReturn("bind_d");
        when(routeRepository.findFirstByBindingId(any())).thenReturn(null);
        when(createRequest.getBoundAppGuid()).thenReturn("app");
        when(createRequest.getBoundRoute()).thenReturn("route");
        when(createRequest.getParameters()).thenReturn(map);
        when(mock(Map.class).values()).thenReturn(map.values());
        bindingService.createServiceInstanceBinding(createRequest);
    }

    @Test
    public void createServiceInstanceBindingTestWithParameters(){
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("filter","1");
        when(createRequest.getServiceInstanceId()).thenReturn("123");
        when(serviceInstanceRepository.findFirstByServiceId(any())).thenReturn(mock(ServiceInstanceEntity.class));
        when(createRequest.getBindingId()).thenReturn("bind_d");
        when(routeRepository.findFirstByBindingId(any())).thenReturn(null);
        when(createRequest.getBoundAppGuid()).thenReturn("app");
        when(createRequest.getBoundRoute()).thenReturn("route");
        when(createRequest.getParameters()).thenReturn(map);
        when(mock(Map.class).values()).thenReturn(map.values());
        when(filterRepository.getOne(1)).thenReturn(mock(Filter.class));
        when(routeRepository.save(any(Route.class))).thenReturn(mock(Route.class));
        when(filterToRouteRepository.save(any(FilterToRoute.class))).thenReturn(mock(FilterToRoute.class));
        CreateServiceInstanceRouteBindingResponse response = bindingService.createServiceInstanceBinding(createRequest);
        Assert.assertNotNull(response);
    }

    @Test
    public void createServiceInstanceBindingTestWithoutParameters(){
        when(createRequest.getServiceInstanceId()).thenReturn("123");
        when(serviceInstanceRepository.findFirstByServiceId(any())).thenReturn(mock(ServiceInstanceEntity.class));
        when(createRequest.getBindingId()).thenReturn("bind_d");
        when(routeRepository.findFirstByBindingId(any())).thenReturn(null);
        when(createRequest.getBoundAppGuid()).thenReturn("app");
        when(createRequest.getBoundRoute()).thenReturn("route");
        when(createRequest.getParameters()).thenReturn(null);
        when(filterRepository.getOne(0)).thenReturn(mock(Filter.class));
        when(routeRepository.save(any(Route.class))).thenReturn(mock(Route.class));
        when(filterToRouteRepository.save(any(FilterToRoute.class))).thenReturn(mock(FilterToRoute.class));
        CreateServiceInstanceRouteBindingResponse response = bindingService.createServiceInstanceBinding(createRequest);
        Assert.assertNotNull(response);
    }

    @Test(expected = ServiceInstanceBindingDoesNotExistException.class)
    public void deleteServiceInstanceBindingTestWithException(){
        when(deleteRequest.getBindingId()).thenReturn("123");
        when(routeRepository.findFirstByBindingId(any())).thenReturn(null);
        bindingService.deleteServiceInstanceBinding(deleteRequest);
    }

}