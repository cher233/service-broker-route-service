package org.servicebroker.routeservice.service;


import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.servicebroker.routeservice.entity.ServiceInstanceEntity;
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


@Service
@Slf4j
public class RouteServiceInstanceService implements ServiceInstanceService {


	@Autowired
	@Setter
	private ServiceInstanceRepository serviceRepository;


	@Override
	public CreateServiceInstanceResponse createServiceInstance(CreateServiceInstanceRequest request) {

		ServiceInstanceEntity instance = getServiceInstance(request.getServiceInstanceId());
		if (instance != null) {
			throw new ServiceInstanceExistsException(request.getServiceInstanceId(), request.getServiceDefinitionId());
		}
		ServiceInstanceEntity newInstance = ServiceInstanceEntity.builder().
				serviceId(request.getServiceInstanceId()).
				planId(request.getPlanId()).
				organizationGuid(request.getOrganizationGuid()).
				spaceGuid(request.getSpaceGuid()).
				build();
		serviceRepository.save(newInstance);
		return new CreateServiceInstanceResponse();
	}

	@Override
	public GetLastServiceOperationResponse getLastOperation(GetLastServiceOperationRequest request) {
		return new GetLastServiceOperationResponse().withOperationState(OperationState.SUCCEEDED);
	}

	protected ServiceInstanceEntity getServiceInstance(String id) {
		return serviceRepository.findFirstByServiceId(id);
	}

	@Override
	public DeleteServiceInstanceResponse deleteServiceInstance(DeleteServiceInstanceRequest request) throws ServiceInstanceDoesNotExistException {
		ServiceInstanceEntity instance = getServiceInstance(request.getServiceInstanceId());
		if (instance == null) {
			throw new ServiceInstanceDoesNotExistException(request.getServiceInstanceId());
		}
		serviceRepository.delete(instance);
		return new DeleteServiceInstanceResponse();
	}

	@Override
	public UpdateServiceInstanceResponse updateServiceInstance(UpdateServiceInstanceRequest request) {

		throw new ServiceInstanceUpdateNotSupportedException("Not available");

	}

}