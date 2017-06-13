package org.servicebroker.routeservice.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.servicebroker.routeservice.entity.ServiceInstanceEntity;
import org.servicebroker.routeservice.repository.ServiceInstanceRepository;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceExistsException;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceRequest;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceResponse;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Nofar on 06/06/2017.
 */
public class RouteServiceInstanceServiceTest {

    private RouteServiceInstanceService routeServiceInstanceService;
    private ServiceInstanceRepository serviceInstanceRepository;
    private CreateServiceInstanceRequest request;

    @Before
    public void init(){
        serviceInstanceRepository = mock(ServiceInstanceRepository.class);
        routeServiceInstanceService = spy(new RouteServiceInstanceService());
        routeServiceInstanceService.setServiceRepository(serviceInstanceRepository);
        request = mock(CreateServiceInstanceRequest.class);
    }

    @Test(expected = ServiceInstanceExistsException.class)
    public void createServiceInstanceWithExceptionTest(){
        when(request.getServiceInstanceId()).thenReturn("123");
        when(request.getServiceDefinitionId()).thenReturn("123");
        when(serviceInstanceRepository.findFirstByServiceId(any())).thenReturn(mock(ServiceInstanceEntity.class));
        routeServiceInstanceService.createServiceInstance(request);
        verify(routeServiceInstanceService).getServiceInstance(any());

    }

    @Test
    public void createServiceInstanceTest(){
        ServiceInstanceEntity.ServiceInstanceEntityBuilder builder = mock(ServiceInstanceEntity.builder().getClass());
        when(serviceInstanceRepository.findFirstByServiceId(any())).thenReturn(null);
        when(request.getServiceInstanceId()).thenReturn("123");
        when(request.getServiceDefinitionId()).thenReturn("123");
        when(request.getPlanId()).thenReturn("plan");
        when(request.getOrganizationGuid()).thenReturn("org");
        when(request.getSpaceGuid()).thenReturn("space");
        when(serviceInstanceRepository.save(any(ServiceInstanceEntity.class))).thenReturn(mock(ServiceInstanceEntity.class));
        CreateServiceInstanceResponse createServiceInstanceResponse = routeServiceInstanceService.createServiceInstance(request);
        verify(routeServiceInstanceService).getServiceInstance(any());
        assertNotNull(createServiceInstanceResponse);
    }

}

