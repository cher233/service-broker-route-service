package org.servicebroker.routeservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.servicebroker.routeservice.entity.Filter;
import org.servicebroker.routeservice.entity.FilterToRoute;
import org.servicebroker.routeservice.entity.Route;
import org.servicebroker.routeservice.entity.ServiceInstanceEntity;
import org.servicebroker.routeservice.repository.FilterRepository;
import org.servicebroker.routeservice.repository.FilterToRouteRepository;
import org.servicebroker.routeservice.repository.RouteRepository;
import org.servicebroker.routeservice.repository.ServiceInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.servicebroker.exception.ServiceBrokerInvalidParametersException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceBindingDoesNotExistException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceBindingExistsException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceDoesNotExistException;
import org.springframework.cloud.servicebroker.model.*;
import org.springframework.cloud.servicebroker.service.ServiceInstanceBindingService;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class RouteServiceInstanceBindingService implements ServiceInstanceBindingService {

	@Autowired
	@Setter
	private RouteRepository routeRepository;

	@Autowired
	@Setter
	private FilterToRouteRepository filterToRouteRepositoryRepository;

	@Autowired
	@Setter
	private ServiceInstanceRepository serviceRepository;

	@Autowired
	@Setter
	private FilterRepository filterRepository;

	@Value("${route.service.url}")
	private String routeURL;

	@Override
	public CreateServiceInstanceBindingResponse createServiceInstanceBinding(CreateServiceInstanceBindingRequest request) {
		ServiceInstanceEntity serviceInstance = validateRequest(request);
		Route routeBinding = Route.builder().
				service(serviceInstance).
				routeName(request.getBoundRoute()).
				bindingId(request.getBindingId()).
				build();
		createFilterToRouteEntry(request.getParameters(), routeBinding, request.getBoundAppGuid());
		return new CreateServiceInstanceRouteBindingResponse().withRouteServiceUrl(routeURL);
	}

	private ServiceInstanceEntity validateRequest(CreateServiceInstanceBindingRequest request)
			throws ServiceInstanceDoesNotExistException, ServiceInstanceBindingExistsException,ServiceBrokerInvalidParametersException
	{
		ServiceInstanceEntity serviceInstance = CheckIfServiceInstanceExist(request.getServiceInstanceId());
		checkIfBindingIdExist(request.getBindingId(),request.getServiceInstanceId());
		if(request.getBoundRoute() == null)
		{
			throw new ServiceBrokerInvalidParametersException("No bound Route exist!");
		}
		return serviceInstance;

	}

	private void checkIfBindingIdExist(String bindingId, String serviceId) throws ServiceInstanceBindingExistsException
	{
		Route routeBinding = routeRepository.findFirstByBindingId(bindingId);
		if (routeBinding != null) {
			throw new ServiceInstanceBindingExistsException(serviceId, bindingId);
		}
	}

	private ServiceInstanceEntity CheckIfServiceInstanceExist(String serviceId)
	{
		ServiceInstanceEntity serviceInstance = serviceRepository.findFirstByServiceId(serviceId);
		if(serviceInstance == null){
			throw  new ServiceInstanceDoesNotExistException(serviceId);
		}
		return serviceInstance;
	}

	protected void createFilterToRouteEntry(Map<String, Object> parameters, Route route, String appGuid) {
		if(parameters == null || parameters.isEmpty())
		{
			FilterToRoute filterToRoute = FilterToRoute.builder().
					route(route).
					filter(filterRepository.getOne(0)).
					appGuid(appGuid).build();
			filterToRouteRepositoryRepository.save(filterToRoute);
		}
		else checkIfValidFilterAndSave(parameters,route, appGuid);
	}

	private void checkIfValidFilterAndSave(Map<String, Object> parameters, Route route, String appGuid)
	{
		List<FilterToRoute> filterToRouteList = new ArrayList<>();
		for (Map.Entry<String, Object> element : parameters.entrySet()) {
			Filter filter = filterRepository.getOne(Integer.getInteger(element.toString()));
			if (filter!= null){
				FilterToRoute filterToRoute = FilterToRoute.builder().
						route(route).
						filter(filter).
						appGuid(appGuid).build();
				filterToRouteList.add(filterToRoute);
			}
			else {
				String error = String.format("Filter id: %s does not exist!",element.toString());
				throw  new ServiceBrokerInvalidParametersException(error);
				}

		}
		routeRepository.save(route);
		filterToRouteRepositoryRepository.save(filterToRouteList);
	}

	@Override
	public void deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest request) {
		Route routeBinding = getServiceInstanceBinding(request.getBindingId());
		if (routeBinding == null) {
			throw new ServiceInstanceBindingDoesNotExistException(request.getBindingId());
		}
		deleteAllFilterToRoute(request.getBindingId());
		routeRepository.delete(routeBinding.getRouteId());
	}

	private void deleteAllFilterToRoute(String bindingId)
	{
		List<FilterToRoute> filterToRouteList = filterToRouteRepositoryRepository.findAllByRoute_BindingId(bindingId);
		if(!filterToRouteList.isEmpty()){
			filterToRouteRepositoryRepository.delete(filterToRouteList);
		}
	}

	private Route getServiceInstanceBinding(String id) {
		return  routeRepository.findFirstByBindingId(id);
}

}
