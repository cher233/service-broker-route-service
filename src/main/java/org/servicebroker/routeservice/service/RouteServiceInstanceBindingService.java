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
	private String routeURL = "";///////////////////////////////////////////////////////To write


	@Override
	public CreateServiceInstanceBindingResponse createServiceInstanceBinding(CreateServiceInstanceBindingRequest request) {

		String bindingId = request.getBindingId();
		ServiceInstanceEntity serviceInstance = serviceRepository.findFirstByServiceId(request.getServiceInstanceId());

		Route routeBinding = routeRepository.findFirstByBindingId(bindingId);
		if (routeBinding != null) {
			throw new ServiceInstanceBindingExistsException(serviceInstance.getServiceId(), bindingId);
		}

		routeBinding = new Route(serviceInstance, request.getBoundRoute(),bindingId);
		//routeRepository.save(routeBinding);
		boolean isValid = createFilterToRouteEntry(request.getParameters(), routeBinding,
				request.getBoundAppGuid());
		if(isValid)
		{
			return new CreateServiceInstanceRouteBindingResponse().withRouteServiceUrl(routeURL);
		}
		else
		{
			return new CreateServiceInstanceRouteBindingResponse();
		}
		//return new CreateServiceInstanceRouteBindingResponse();
	}

	private boolean createFilterToRouteEntry(Map<String, Object> parameters, Route route, String appGuid) {
		if(parameters == null)
		{
			filterRepository.save(new FilterToRoute(FiltersType.DEFAULT, route, appGuid));
			return true;
		}
		else return checkIfValidFilterAndSave(parameters,route, appGuid);
	}

	private boolean checkIfValidFilterAndSave(Map<String, Object> parameters, Route route, String appGuid)
	{
		for (Map.Entry<String, Object> element : parameters.entrySet()) {
			String filter = ((String) element.getValue()).toUpperCase();
			if (FiltersType.contains(filter))
			{
				FiltersType filterId = FiltersType.valueOf(filter);
				filterRepository.save(new FilterToRoute(filterId, route, appGuid));
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
