package org.servicebroker.routeservice.service;

import java.util.Map;

import org.servicebroker.routeservice.entity.FilterToRoute;
import org.servicebroker.routeservice.entity.Route;
import org.servicebroker.routeservice.entity.ServiceInstanceEntity;
import org.servicebroker.routeservice.model.FiltersType;
import org.servicebroker.routeservice.repository.FilterToRouteRepository;
import org.servicebroker.routeservice.repository.RouteRepository;
import org.servicebroker.routeservice.repository.ServiceInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.servicebroker.exception.ServiceBrokerInvalidParametersException;
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

	@Autowired
	private RouteRepository routeRepository;

	@Autowired
	private FilterToRouteRepository filterRepository;

	@Autowired
	private ServiceInstanceRepository serviceRepository;

	@Value("${route.service.url}")
	private String routeURL;


	@Override
	public CreateServiceInstanceBindingResponse createServiceInstanceBinding(CreateServiceInstanceBindingRequest request) {

		String bindingId = request.getBindingId();
		ServiceInstanceEntity serviceInstance = serviceRepository.findFirstByServiceId(request.getServiceInstanceId());
		//TODO check serviceInstance null

		Route routeBinding = routeRepository.findFirstByBindingId(bindingId);
		if (routeBinding != null) {
			throw new ServiceInstanceBindingExistsException(serviceInstance.getServiceId(), bindingId);
		}

		routeBinding = new Route(serviceInstance, request.getBoundRoute(), bindingId);
		boolean isValid = createFilterToRouteEntry(request.getParameters(), routeBinding, request.getBoundAppGuid());


		if(isValid) {
			return new CreateServiceInstanceRouteBindingResponse().withRouteServiceUrl(routeURL);
		}
		else {
			return new CreateServiceInstanceRouteBindingResponse();
		}
	}

	private boolean createFilterToRouteEntry(Map<String, Object> parameters, Route route, String appGuid) {
		if(parameters == null || parameters.isEmpty()) {
			FilterToRoute filterToRoute = FilterToRoute.builder()
					.filter(FiltersType.DEFAULT)
					.route(route)
					.appGuid(appGuid)
					.build();
			filterRepository.save(filterToRoute);
			return true;
		}
		return checkIfValidFilterAndSave(parameters,route, appGuid);
	}

	private boolean checkIfValidFilterAndSave(Map<String, Object> parameters, Route route, String appGuid) {
		for (Object element : parameters.values()) {
			String filter = element.toString().toUpperCase();
			if (FiltersType.contains(filter)) {
				FiltersType filterId = FiltersType.valueOf(filter);
				FilterToRoute filterToRoute = FilterToRoute.builder()
						.filter(filterId)
						.route(route)
						.appGuid(appGuid)
						.build();
				filterRepository.save(filterToRoute);
			}
			else return  false;
		}
		return true;
	}

	@Override
	public void deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest request) {
		Route routeBinding = getServiceInstanceBinding(request.getBindingId());
		if (routeBinding == null) {
			throw new ServiceInstanceBindingDoesNotExistException(routeBinding.getBindingId());
		}
		routeRepository.delete(routeBinding.getRouteId());
	}

	protected Route getServiceInstanceBinding(String id) {
		return  routeRepository.findFirstByBindingId(id);
	}
}
