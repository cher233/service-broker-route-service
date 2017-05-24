package org.servicebroker.routeservice.service;

import java.util.Map;

import org.springframework.cloud.servicebroker.model.*;
import org.springframework.cloud.servicebroker.service.ServiceInstanceBindingService;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class RouteServiceInstanceBindingService implements ServiceInstanceBindingService {


	private String routeURL = "";///////////////////////////////////////////////////////To write

//	@Autowired
//	public RouteServiceInstanceBindingService( bindingRepository) {
//		this.bindingRepository = bindingRepository;
//	}
	
	@Override
	public CreateServiceInstanceBindingResponse createServiceInstanceBinding(CreateServiceInstanceBindingRequest request) {

		String bindingId = request.getBindingId();
		String serviceInstanceId = request.getServiceInstanceId();
		Map<String, Object> bindResources = request.getBindResource();

//		ServiceInstanceBinding binding = bindingRepository.findOne(bindingId);
//		if (binding != null) {
//			throw new ServiceInstanceBindingExistsException(serviceInstanceId, bindingId);
//		}
//
//
//		binding = new ServiceInstanceBinding(bindingId, serviceInstanceId, bindResources, null, request.getBoundAppGuid());
//		bindingRepository.save(binding);
		
		return new CreateServiceInstanceRouteBindingResponse().withRouteServiceUrl(routeURL);
	}

	@Override
	public void deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest request) {
		String bindingId = request.getBindingId();
		//ServiceInstanceBinding binding = getServiceInstanceBinding(bindingId);

//		if (binding == null) {
//			throw new ServiceInstanceBindingDoesNotExistException(bindingId);
//		}

		//bindingRepository.delete(bindingId);
	}

//	protected ServiceInstanceBinding getServiceInstanceBinding(String id) {
//		return new ServiceInstanceBinding(id, "a",new Collections.emptyMap());
//	}

}
