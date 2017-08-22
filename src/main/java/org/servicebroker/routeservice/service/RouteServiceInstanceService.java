package org.servicebroker.routeservice.service;


import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.servicebroker.routeservice.entity.ServiceInstanceEntity;
import org.servicebroker.routeservice.repository.ServiceInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.exception.*;
import org.springframework.cloud.servicebroker.model.*;
import org.springframework.cloud.servicebroker.service.CatalogService;
import org.springframework.cloud.servicebroker.service.ServiceInstanceService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class RouteServiceInstanceService implements ServiceInstanceService {


	@Autowired
	@Setter
	private ServiceInstanceRepository serviceRepository;

	@Autowired
	@Setter
	CatalogService catalogService;

	@Autowired
	@Setter
	PasswordEncoder passwordEncoder;

	@Override

	public CreateServiceInstanceResponse createServiceInstance(CreateServiceInstanceRequest request) {
		log.info("Validating request: {}", request.toString());
		ServiceInstanceEntity instance = getServiceInstance(request.getServiceInstanceId());
		if (instance != null) {
			throw new ServiceInstanceExistsException(request.getServiceInstanceId(), request.getServiceDefinitionId());
		}
		ServiceDefinition sd = catalogService.getServiceDefinition(request
				.getServiceDefinitionId());
		checkIfDefinitionIdAndPlanIdExist(sd,request);
		String password=checkIfExistAndHashPassword(request.getParameters());
		ServiceInstanceEntity newInstance = ServiceInstanceEntity.builder()
				.serviceId(request.getServiceInstanceId())
				.planId(request.getPlanId())
				.organizationGuid(request.getOrganizationGuid())
				.password(password)
				.spaceGuid(request.getSpaceGuid())
				.build();
		serviceRepository.save(newInstance);
		log.info("saving instance {}", newInstance);
		return new CreateServiceInstanceResponse();
	}

	private String checkIfExistAndHashPassword(Map<String,Object> params){
		Object password = params.get("password");
		if (password == null) {
			throw  new ServiceBrokerInvalidParametersException("password was not provided");
		}
		String hashedPassword = passwordEncoder.encode(password.toString());
		return hashedPassword;
	}

	private void checkIfDefinitionIdAndPlanIdExist(ServiceDefinition serviceDefinition,CreateServiceInstanceRequest request ) throws ServiceInstanceBindingExistsException {
		List<Plan> planList = serviceDefinition.getPlans();
		String planToMatch = request.getPlanId();
		boolean result = false;
		if (serviceDefinition == null)
			throw new ServiceBrokerException(
					"Unable to find service definition with id: " + request.getServiceDefinitionId());
		for(Plan plan : planList){
			if(plan.getId().equals(planToMatch)){
				result = true;
				break;
			}
		}

		if (!result)
			throw new ServiceBrokerException(
					"Unable to find plan: " + request.getPlanId());
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
		log.debug("Checking that instance: {} exists.",request.getServiceInstanceId());
		ServiceInstanceEntity instance = getServiceInstance(request.getServiceInstanceId());
		if (instance == null) {
			throw new ServiceInstanceDoesNotExistException(request.getServiceInstanceId());
		}
		log.debug("Stating to delete filters entries.");
		serviceRepository.delete(instance.getId());
		log.info("Delete successful, delete data: {}", instance);
		return new DeleteServiceInstanceResponse();
	}

	@Override
	public UpdateServiceInstanceResponse updateServiceInstance(UpdateServiceInstanceRequest request) {
		throw new ServiceInstanceUpdateNotSupportedException("This service is not updatable");
	}
}