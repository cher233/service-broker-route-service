package org.servicebroker.routeservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
	public CreateServiceInstanceRouteBindingResponse createServiceInstanceBinding(CreateServiceInstanceBindingRequest request) {
		log.info("Validating request. \n");
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
		log.debug("Passed all validations./n");
		return serviceInstance;

	}

	private void checkIfBindingIdExist(String bindingId, String serviceId) throws ServiceInstanceBindingExistsException
	{
		log.debug("Validating that binding id doesn't exist in db.\n");
		Route routeBinding = routeRepository.findFirstByBindingId(bindingId);
		if (routeBinding != null) {
			throw new ServiceInstanceBindingExistsException(serviceId, bindingId);
		}
	}

	private ServiceInstanceEntity CheckIfServiceInstanceExist(String serviceId)
	{
		log.debug("Validating that service instance exist.");
		ServiceInstanceEntity serviceInstance = serviceRepository.findFirstByServiceId(serviceId);
		if(serviceInstance == null){
			throw  new ServiceInstanceDoesNotExistException(serviceId);
		}
		return serviceInstance;
	}

	private void createFilterToRouteEntry(Map<String, Object> parameters, Route route, String appGuid) {
		log.info("Checking for filters.\n");
		if(parameters == null || parameters.isEmpty())
		{
			FilterToRoute filterToRoute = FilterToRoute.builder().
					route(route).
					filter(filterRepository.getOne(0)).
					appGuid(appGuid).build();
			log.info("Save successful, saved data:\n {}\n", routeRepository.save(route).toString());
			log.info("Save successful, saved data:\n {}\n", filterToRouteRepositoryRepository.save(filterToRoute).toString());
		}
		else checkIfValidFilterAndSave(parameters,route, appGuid);
	}

	private void checkIfValidFilterAndSave(Map<String, Object> parameters, Route route, String appGuid)
	{
		Filter filter = null;
		List<FilterToRoute> filterToRouteList = new ArrayList<>();
		for (Object element : parameters.values()) {
			log.debug("Extracting filters.\n");
			String stringToCheck = element.toString();
			if(stringToCheck.matches("^\\d+$")){
				int id = Integer.parseInt(stringToCheck);
				filter = filterRepository.getOne(id);
			}
			if (filter!= null){
				filterToRouteList.add(FilterToRoute.builder().
						route(route).
						filter(filter).
						appGuid(appGuid).build());
			}
			else {
				String error = String.format("Filter id: %s does not exist!",element.toString());
				throw  new ServiceBrokerInvalidParametersException(error);
				}

		}
		log.info("Save successful, saved data:\n {}\n",routeRepository.save(route).toString());
		log.info("Save successful, saved data:\n {}\n",filterToRouteRepositoryRepository.save(filterToRouteList).toString());
	}

	@Override
	public void deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest request) {
		log.debug("Checking that binding id: {} exists.\n",request.getBindingId());
		Route routeBinding = getServiceInstanceBinding(request.getBindingId());
		if (routeBinding == null) {
			throw new ServiceInstanceBindingDoesNotExistException(request.getBindingId());
		}
		log.debug("Stating to delete filters entries.\n");
		deleteAllFilterToRoute(request.getBindingId());
		routeRepository.delete(routeBinding.getRouteId());
		log.info("Delete successful, delete data:\n {}\n", routeBinding);
	}

	private void deleteAllFilterToRoute(String bindingId)
	{
		List<FilterToRoute> filterToRouteList = filterToRouteRepositoryRepository.findAllByRoute_BindingId(bindingId);
		if(!filterToRouteList.isEmpty()){
			filterToRouteRepositoryRepository.delete(filterToRouteList);
			log.info("Delete successful, delete data:\n {}\n", filterToRouteList);
		}
	}

	private Route getServiceInstanceBinding(String id) {
		return  routeRepository.findFirstByBindingId(id);
}

}
