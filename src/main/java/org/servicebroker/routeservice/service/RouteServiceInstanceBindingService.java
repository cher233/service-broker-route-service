package org.servicebroker.routeservice.service;

import java.util.Map;

import org.servicebroker.routeservice.entity.Route;
import org.servicebroker.routeservice.repository.FilterToRouteRepository;
import org.servicebroker.routeservice.repository.RouteRepository;
import org.servicebroker.routeservice.repository.ServiceInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceBindingDoesNotExistException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceBindingExistsException;
import org.springframework.cloud.servicebroker.model.*;
import org.springframework.cloud.servicebroker.service.ServiceInstanceBindingService;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class RouteServiceInstanceBindingService implements ServiceInstanceBindingService {

	private RouteRepository routeRepository;
	private FilterToRouteRepository filterRepository;
	private ServiceInstanceRepository serviceRepository;
	private String routeURL = "";///////////////////////////////////////////////////////To write

	@Autowired
	public RouteServiceInstanceBindingService(  RouteRepository routeRepository, FilterToRouteRepository filterRepository,
												ServiceInstanceRepository serviceRepository )
	{
		this.routeRepository = routeRepository;
		this.filterRepository = filterRepository;
		this.serviceRepository  = serviceRepository;
	}
	
	@Override
	public CreateServiceInstanceBindingResponse createServiceInstanceBinding(CreateServiceInstanceBindingRequest request) {

		String bindingId = request.getBindingId();
		String serviceInstanceId = request.getServiceInstanceId();
		long serviceId =  1; //needs to change once rep is complete
		Map<String, Object> parameters = request.getParameters();
		String boundRoute = request.getBoundRoute();



		Route routeBinding = routeRepository.findByServiceId(serviceId);
		if (routeBinding != null) {
			throw new ServiceInstanceBindingExistsException(serviceInstanceId, bindingId);
		}

		routeBinding = new Route(serviceId,boundRoute);
		routeRepository.save(routeBinding);
		createFilterToRouteEntry(parameters,routeBinding.getRouteId(), request.getBoundAppGuid());
		return new CreateServiceInstanceRouteBindingResponse().withRouteServiceUrl(routeURL);
	}

	private void createFilterToRouteEntry(Map<String, Object> parameters, long routeId, String appGuid)
	{
		//foreach()
//		FilterToRoute filterEntry = new FilterToRoute(new FilterToRouteKey () , appGuid);

		//filterRepository.save(filterEntry);
	}

	@Override
	public void deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest request) {
		String bindingId = request.getBindingId();
		//ServiceInstanceBinding binding = getServiceInstanceBinding(bindingId);
		long serviceId =  1; //needs to change once rep is complete
		Route routeBinding = routeRepository.findByServiceId(serviceId);
		if (routeBinding == null) {
			throw new ServiceInstanceBindingDoesNotExistException(bindingId);
		}
		routeRepository.delete(routeBinding.getRouteId());
	}

//	protected ServiceInstanceBinding getServiceInstanceBinding(String id) {
//		return new ServiceInstanceBinding(id, "a",new Collections.emptyMap());
//	}

}
