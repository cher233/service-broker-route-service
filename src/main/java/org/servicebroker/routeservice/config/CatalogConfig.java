package org.servicebroker.routeservice.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.servicebroker.model.Catalog;
import org.springframework.cloud.servicebroker.model.Plan;
import org.springframework.cloud.servicebroker.model.ServiceDefinition;
import org.springframework.cloud.servicebroker.model.ServiceDefinitionRequires;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatalogConfig {

	@Bean
	public Catalog catalog() {
		return new Catalog(Collections.singletonList(
				new ServiceDefinition(
						"route-service-broker",
						"route service",
						"A simple route service broker implementation",
						true,
						false,
						Collections.singletonList(
								new Plan("route-service-plan",
										"default",
										"default plan for route sercvice",
										getPlanMetadata())),
						null,
						null,
						Arrays.asList(ServiceDefinitionRequires.SERVICE_REQUIRES_ROUTE_FORWARDING.toString()),
						null)));
	}

/* Used by Pivotal CF console */

	private Map<String,Object> getPlanMetadata() {
		Map<String,Object> planMetadata = new HashMap<>();
		planMetadata.put("costs", getCosts());
		return planMetadata;
	}

	private List<Map<String,Object>> getCosts() {
		Map<String,Object> costsMap = new HashMap<>();

		Map<String,Object> amount = new HashMap<>();
		amount.put("usd", 0.0);

		costsMap.put("amount", amount);
		costsMap.put("unit", "MONTHLY");

		return Collections.singletonList(costsMap);
	}

}