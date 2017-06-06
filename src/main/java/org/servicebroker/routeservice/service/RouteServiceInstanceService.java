package org.servicebroker.routeservice.service;


import org.servicebroker.routeservice.entity.ServiceInstanceEntity;
import org.servicebroker.routeservice.model.ServiceInstance;
import org.servicebroker.routeservice.repository.ServiceInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceDoesNotExistException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceExistsException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceUpdateNotSupportedException;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceRequest;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceResponse;
import org.springframework.cloud.servicebroker.model.DeleteServiceInstanceRequest;
import org.springframework.cloud.servicebroker.model.DeleteServiceInstanceResponse;
import org.springframework.cloud.servicebroker.model.GetLastServiceOperationRequest;
import org.springframework.cloud.servicebroker.model.GetLastServiceOperationResponse;
import org.springframework.cloud.servicebroker.model.OperationState;
import org.springframework.cloud.servicebroker.model.UpdateServiceInstanceRequest;
import org.springframework.cloud.servicebroker.model.UpdateServiceInstanceResponse;
import org.springframework.cloud.servicebroker.service.ServiceInstanceService;
import org.springframework.stereotype.Service;

import java.util.Objects;


/**
 */
@Service
public class RouteServiceInstanceService implements ServiceInstanceService {


	@Autowired
	private ServiceInstanceRepository serviceRepository;


	@Override
	public CreateServiceInstanceResponse createServiceInstance(CreateServiceInstanceRequest request) {

		ServiceInstanceEntity instance = getServiceInstance(request.getServiceInstanceId());
		if (instance != null) {
			throw new ServiceInstanceExistsException(request.getServiceInstanceId(), request.getServiceDefinitionId());
		}
		ServiceInstanceEntity updatedInstanc = new ServiceInstanceEntity(request.getServiceInstanceId(),request.getPlanId()
				,request.getOrganizationGuid(),request.getSpaceGuid());
		serviceRepository.save(updatedInstanc);
		return new CreateServiceInstanceResponse();
	}

	@Override
	public GetLastServiceOperationResponse getLastOperation(GetLastServiceOperationRequest request) {
		return new GetLastServiceOperationResponse().withOperationState(OperationState.SUCCEEDED);
	}

	public ServiceInstanceEntity getServiceInstance(String id) {
		return serviceRepository.findFirstByServiceId(id);
	}

	@Override
	public DeleteServiceInstanceResponse deleteServiceInstance(DeleteServiceInstanceRequest request) throws ServiceInstanceDoesNotExistException {
		ServiceInstanceEntity instance = getServiceInstance(request.getServiceInstanceId());
		if (instance == null) {
			throw new ServiceInstanceDoesNotExistException(instance.getServiceId());
		}
		serviceRepository.delete(instance.getId());
		return new DeleteServiceInstanceResponse();
	}

	@Override
	public UpdateServiceInstanceResponse updateServiceInstance(UpdateServiceInstanceRequest request) {

		throw new ServiceInstanceUpdateNotSupportedException("Not available");

	}

}