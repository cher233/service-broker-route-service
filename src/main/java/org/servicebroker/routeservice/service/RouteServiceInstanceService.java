package org.servicebroker.routeservice.service;


import org.servicebroker.routeservice.model.ServiceInstance;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceDoesNotExistException;
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


	
	//private  repository;
	
//	@Autowired
//	public RouteServiceInstanceService( serviceRepository) {
//		this.repository = serviceRepository;
//	}
	
	@Override
	public CreateServiceInstanceResponse createServiceInstance(CreateServiceInstanceRequest request) {
//		ServiceInstance instance = repository.findOne(request.getServiceInstanceId());
//		if (instance != null) {
//			throw new ServiceInstanceExistsException(request.getServiceInstanceId(), request.getServiceDefinitionId());
//		}

//		instance = new ServiceInstance(request);

	//	repository.save(instance);

		return new CreateServiceInstanceResponse();
	}

	@Override
	public GetLastServiceOperationResponse getLastOperation(GetLastServiceOperationRequest request) {
		return new GetLastServiceOperationResponse().withOperationState(OperationState.SUCCEEDED);
	}

	public ServiceInstance getServiceInstance(String id) {
		return null;
	}

	@Override
	public DeleteServiceInstanceResponse deleteServiceInstance(DeleteServiceInstanceRequest request) throws ServiceInstanceDoesNotExistException {
		String instanceId = request.getServiceInstanceId();
	//	ServiceInstance instance = repository.findOne(instanceId);
		Objects x = null;
		if ( x != null) {
			throw new ServiceInstanceDoesNotExistException(instanceId);
		}

//		repository.delete(instanceId);
		return new DeleteServiceInstanceResponse();
	}

	@Override
	public UpdateServiceInstanceResponse updateServiceInstance(UpdateServiceInstanceRequest request) {
		String instanceId = request.getServiceInstanceId();
		ServiceInstance instance = null;
		if (instance != null) {
			throw new ServiceInstanceDoesNotExistException(instanceId);
		}

//		repository.delete(instanceId);
//		ServiceInstance updatedInstance = new ServiceInstance(request);
//		repository.save(updatedInstance);
		return new UpdateServiceInstanceResponse();
	}

}