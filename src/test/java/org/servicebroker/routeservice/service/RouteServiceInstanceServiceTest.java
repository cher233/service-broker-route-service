package org.servicebroker.routeservice.service;

import org.junit.Before;
import org.junit.Test;
import org.servicebroker.routeservice.entity.ServiceInstanceEntity;
import org.servicebroker.routeservice.repository.ServiceInstanceRepository;
import org.springframework.cloud.servicebroker.exception.ServiceBrokerInvalidParametersException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceDoesNotExistException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceExistsException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceUpdateNotSupportedException;
import org.springframework.cloud.servicebroker.model.*;
import org.springframework.cloud.servicebroker.service.CatalogService;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.*;
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
    private CatalogService catalogService;
    private PasswordEncoder passwordEncoder;

    @Before
    public void init() {
        serviceInstanceRepository = mock(ServiceInstanceRepository.class);
        routeServiceInstanceService = spy(new RouteServiceInstanceService());
        routeServiceInstanceService.setServiceRepository(serviceInstanceRepository);
        catalogService  = mock(CatalogService.class);
        routeServiceInstanceService.setCatalogService(catalogService);
        passwordEncoder = mock(PasswordEncoder.class);
        routeServiceInstanceService.setPasswordEncoder(passwordEncoder);
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
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("password","134");
        ServiceDefinition serviceDefinition = mock(ServiceDefinition.class);
        requestCreate = mock(CreateServiceInstanceRequest.class);
        List<Plan> plans = new ArrayList<>();
        Plan plan = mock(Plan.class);
        plans.add(plan);
        when(requestCreate.getServiceInstanceId()).thenReturn("123");
        when(serviceInstanceRepository.findFirstByServiceId(any())).thenReturn(null);
        when(requestCreate.getServiceDefinitionId()).thenReturn("route-service");
        when(catalogService.getServiceDefinition(any())).thenReturn(serviceDefinition);
        when(serviceDefinition.getPlans()).thenReturn(plans);
        when(requestCreate.getPlanId()).thenReturn("plan");
        when(plan.getId()).thenReturn("plan");
        when(requestCreate.getParameters()).thenReturn(map);
        when(mock(Map.class).get(any())).thenReturn(map.get("password"));
        when(passwordEncoder.encode("134")).thenReturn("134");
        when(requestCreate.getOrganizationGuid()).thenReturn("org");
        when(requestCreate.getSpaceGuid()).thenReturn("space");
        when(serviceInstanceRepository.save(any(ServiceInstanceEntity.class))).thenReturn(mock(ServiceInstanceEntity.class));
        CreateServiceInstanceResponse createServiceInstanceResponse = routeServiceInstanceService.createServiceInstance(requestCreate);
        verify(routeServiceInstanceService).getServiceInstance(any());
        assertNotNull(createServiceInstanceResponse);
    }

    @Test(expected = ServiceBrokerInvalidParametersException.class)
    public void createServiceInstanceTestWithoutPassword() {
        requestCreate = mock(CreateServiceInstanceRequest.class);
        ServiceDefinition serviceDefinition = mock(ServiceDefinition.class);
        List<Plan> plans = new ArrayList<>();
        Plan plan = mock(Plan.class);
        plans.add(plan);
        when(requestCreate.getServiceInstanceId()).thenReturn("1");
        when(serviceInstanceRepository.findFirstByServiceId(any())).thenReturn(null);
        when(requestCreate.getServiceDefinitionId()).thenReturn("route-service");
        when(catalogService.getServiceDefinition("route-service")).thenReturn(serviceDefinition);
        when(serviceDefinition.getPlans()).thenReturn(plans);
        when(requestCreate.getPlanId()).thenReturn("plan");
        when(plan.getId()).thenReturn("plan");
        when(requestCreate.getParameters()).thenReturn(mock(Map.class));
        when(mock(Map.class).get("123")).thenReturn(null);
        routeServiceInstanceService.createServiceInstance(requestCreate);
        verify(routeServiceInstanceService).getServiceInstance(any());
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
