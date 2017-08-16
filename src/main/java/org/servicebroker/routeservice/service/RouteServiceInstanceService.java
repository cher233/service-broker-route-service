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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RouteServiceInstanceService implements ServiceInstanceService {


	@Autowired
	@Setter
	private ServiceInstanceRepository serviceRepository;

	@Autowired
	@Setter
	CatalogService catalogService;

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

		ServiceInstanceEntity newInstance = ServiceInstanceEntity.builder()
				.serviceId(request.getServiceInstanceId())
				.planId(request.getPlanId())
				.organizationGuid(request.getOrganizationGuid())
				.spaceGuid(request.getSpaceGuid())
				.build();
		serviceRepository.save(newInstance);
		log.info("saving instance {}", newInstance);
		return new CreateServiceInstanceResponse();
	}

	private void checkIfDefinitionIdAndPlanIdExist(ServiceDefinition serviceDefinition,CreateServiceInstanceRequest request ) throws ServiceInstanceBindingExistsException {
		List<Plan> getPlan = serviceDefinition.getPlans();

		if (serviceDefinition == null) {
			throw new ServiceBrokerException(
					"Unable to find service definition with id: "
							+ request.getServiceDefinitionId());
		}

		if (getPlan == null) {
			throw new ServiceBrokerException(
					"Unable to find plan: "
							+ request.getPlanId());
		}
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