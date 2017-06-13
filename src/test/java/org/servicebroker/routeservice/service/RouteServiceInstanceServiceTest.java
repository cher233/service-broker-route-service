package org.servicebroker.routeservice.service;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.servicebroker.routeservice.entity.ServiceInstanceEntity;
import org.servicebroker.routeservice.repository.ServiceInstanceRepository;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceDoesNotExistException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceExistsException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceUpdateNotSupportedException;
import org.springframework.cloud.servicebroker.model.*;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Nofar on 06/06/2017.
 */
public class RouteServiceInstanceServiceTest {

    private RouteServiceInstanceService routeServiceInstanceService;
    private ServiceInstanceRepository serviceInstanceRepository;
    private CreateServiceInstanceRequest requestCreate;
    private GetLastServiceOperationRequest requestGet;
    private DeleteServiceInstanceRequest requestDelete;
    private UpdateServiceInstanceRequest requestUpdate;

    @Before
    public void init() {
        serviceInstanceRepository = mock(ServiceInstanceRepository.class);
        routeServiceInstanceService = spy(new RouteServiceInstanceService());
        routeServiceInstanceService.setServiceRepository(serviceInstanceRepository);
    }

    @Test(expected = ServiceInstanceExistsException.class)
    public void createServiceInstanceWithExceptionTest() {
        requestCreate = mock(CreateServiceInstanceRequest.class);
        when(requestCreate.getServiceInstanceId()).thenReturn("123");
        when(serviceInstanceRepository.findFirstByServiceId(any())).thenReturn(mock(ServiceInstanceEntity.class));
        routeServiceInstanceService.createServiceInstance(requestCreate);
        verify(routeServiceInstanceService).getServiceInstance(any());
    }

    @Test
    public void createServiceInstanceTest() {
        requestCreate = mock(CreateServiceInstanceRequest.class);
        when(requestCreate.getServiceInstanceId()).thenReturn("123");
        when(serviceInstanceRepository.findFirstByServiceId(any())).thenReturn(null);
        when(requestCreate.getPlanId()).thenReturn("plan");
        when(requestCreate.getOrganizationGuid()).thenReturn("org");
        when(requestCreate.getSpaceGuid()).thenReturn("space");
        when(serviceInstanceRepository.save(any(ServiceInstanceEntity.class))).thenReturn(mock(ServiceInstanceEntity.class));
        CreateServiceInstanceResponse createServiceInstanceResponse = routeServiceInstanceService.createServiceInstance(requestCreate);
        verify(routeServiceInstanceService).getServiceInstance(any());
        assertNotNull(createServiceInstanceResponse);
    }

    @Test
    public void getLastServiceOperationTest() {
        requestGet = mock(GetLastServiceOperationRequest.class);
        GetLastServiceOperationResponse getLastServiceOperationResponse = routeServiceInstanceService.getLastOperation(requestGet);
        assertNotNull(getLastServiceOperationResponse);
    }


    @Test(expected = ServiceInstanceDoesNotExistException.class)
    public void deleteServiceInstanceWithExceptionTest() {
        requestDelete = mock(DeleteServiceInstanceRequest.class);
        when(requestDelete.getServiceInstanceId()).thenReturn("123");
        when(serviceInstanceRepository.findFirstByServiceId(any())).thenReturn(null);
        routeServiceInstanceService.deleteServiceInstance(requestDelete);
        verify(routeServiceInstanceService).getServiceInstance(any());
    }

    @Test
    public void deleteServiceInstanceTest() {
        requestDelete = mock(DeleteServiceInstanceRequest.class);
        when(requestDelete.getServiceInstanceId()).thenReturn("123");
        when(serviceInstanceRepository.findFirstByServiceId(any())).thenReturn(mock(ServiceInstanceEntity.class));
        DeleteServiceInstanceResponse deleteServiceInstanceResponse = routeServiceInstanceService.deleteServiceInstance(requestDelete);
        verify(routeServiceInstanceService).getServiceInstance(any());
        assertNotNull(deleteServiceInstanceResponse);
    }

    @Test(expected = ServiceInstanceUpdateNotSupportedException.class)
    public void updateServiceInstanceWithExceptionTest() {
        requestUpdate=mock(UpdateServiceInstanceRequest.class);
        routeServiceInstanceService.updateServiceInstance(requestUpdate);
    }
}