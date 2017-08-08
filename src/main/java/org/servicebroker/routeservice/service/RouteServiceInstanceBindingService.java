package org.servicebroker.routeservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
/*import org.cher.entities.FilterEntity;
import org.cher.entities.FilterToRoute;
import org.cher.entities.Route;
import org.cher.entities.ServiceInstanceEntity;*/
import org.servicebroker.routeservice.entity.FilterEntity;
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
		log.info("Validating request: {}", request.toString());
		ServiceInstanceEntity serviceInstance = validateRequest(request);
		Route routeBinding = Route.builder().
				service(serviceInstance).
				routeName(request.getBoundRoute()).
				bindingId(request.getBindingId()).
				build();
		createFilterToRouteEntry(request.getParameters(), routeBinding, request.getBoundAppGuid());
		return new CreateServiceInstanceRouteBindingResponse();
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
		log.debug("Passed all validations.");
		return serviceInstance;

	}

	private void checkIfBindingIdExist(String bindingId, String serviceId) throws ServiceInstanceBindingExistsException
	{
		log.debug("Checking that binding id {} doesn't exist in db.",bindingId);
		Route routeBinding = routeRepository.findFirstByBindingId(bindingId);
		if (routeBinding != null) {
			throw new ServiceInstanceBindingExistsException(serviceId, bindingId);
		}
	}

	private ServiceInstanceEntity CheckIfServiceInstanceExist(String serviceId)
	{
		log.debug("Validating that service instance {} exist.", serviceId);
		ServiceInstanceEntity serviceInstance = serviceRepository.findFirstByServiceId(serviceId);
		if(serviceInstance == null){
			throw  new ServiceInstanceDoesNotExistException(serviceId);
		}
		return serviceInstance;
	}

	private void createFilterToRouteEntry(Map<String, Object> parameters, Route route, String appGuid) {
		log.debug("Checking if filters exist...\n");
		if(parameters == null || parameters.isEmpty())
		{
			FilterToRoute filterToRoute = FilterToRoute.builder().
					route(route).
					filter(filterRepository.getOne(0)).
					appGuid(appGuid).build();
			log.info("Saving route...");
			routeRepository.save(route).toString();
			log.info("Save successful, saved data:\n {}",route.toString() );
			log.info("Saving filters for route...");
			filterToRouteRepositoryRepository.save(filterToRoute);
			log.info("Save successful, saved data:\n {}",filterToRoute.toString());
		}
		else checkIfValidFilterAndSave(parameters,route, appGuid);
	}

	private void checkIfValidFilterAndSave(Map<String, Object> parameters, Route route, String appGuid)
	{
		List<FilterToRoute> filterToRouteList = new ArrayList<>();
		for (Object element : parameters.values()) {
			log.debug("Extracting filters.\n");
			try {
				FilterEntity filter = filterRepository.getOne(Integer.parseInt(element.toString()));
				if (filter!= null){
				filterToRouteList.add(FilterToRoute.builder().
						route(route).
						filter(filter).
						appGuid(appGuid).build());
				}
				else {
					String error = String.format("FilterEntity id: %s does not exist!",element.toString());
					throw  new ServiceBrokerInvalidParametersException(error);
				}
			}
			catch (Exception e) {
				String error = String.format("FilterEntity id: %s isn't valid!",element.toString());
				throw  new ServiceBrokerInvalidParametersException(error);
			}
		}
		log.info("Saving route...");
		routeRepository.save(route);
		log.info("Save successful, saved data:\n {}",route.toString());
		log.info("Saving filters for route...");
		filterToRouteRepositoryRepository.save(filterToRouteList);
		log.info("Save successful, saved data:\n {}",filterToRouteList.toString());
	}

	@Override
	public void deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest request) {
		log.debug("Checking that binding id: {} exists for service id {}.",request.getBindingId(), request.getServiceInstanceId());
		Route routeBinding = getServiceInstanceBinding(request.getBindingId());
		if (routeBinding == null) {
			throw new ServiceInstanceBindingDoesNotExistException(request.getBindingId());
		}
		log.debug("Stating to delete filters entries.");
		deleteAllFilterToRoute(request.getBindingId());
		log.info("Deleting route...");
		routeRepository.delete(routeBinding.getRouteId());
		log.info("Delete successful, delete data:\n {}", routeBinding.toString());
	}

	private void deleteAllFilterToRoute(String bindingId)
	{
		List<FilterToRoute> filterToRouteList = filterToRouteRepositoryRepository.findAllByRoute_BindingId(bindingId);
		if(!filterToRouteList.isEmpty()){
			log.info("Deleting filter for route...");
			filterToRouteRepositoryRepository.delete(filterToRouteList);
			log.info("Delete successful, deleted data:\n {}", filterToRouteList.toString());
		}
	}

	private Route getServiceInstanceBinding(String id) {
		return  routeRepository.findFirstByBindingId(id);
}

}
