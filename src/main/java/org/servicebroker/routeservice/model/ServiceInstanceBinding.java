package org.servicebroker.routeservice.model;

import java.util.HashMap;
import java.util.Map;

/**
 * A binding to a service instance
 *
 * @author sgreenberg@pivotal.io
 */
public class ServiceInstanceBinding {

	private String id;
	private String serviceInstanceId;
	private Map<String,Object> bindResource = new HashMap<>();
	private String syslogDrainUrl;
	private String appGuid;

	public ServiceInstanceBinding(String id,
								  String serviceInstanceId,
								  Map<String,Object> resources,
								  String syslogDrainUrl, String appGuid) {
		this.id = id;
		this.serviceInstanceId = serviceInstanceId;
		setBindResource(resources);
		this.syslogDrainUrl = syslogDrainUrl;
		this.appGuid = appGuid;
	}

	public String getId() {
		return id;
	}

	public String getServiceInstanceId() {
		return serviceInstanceId;
	}

	public Map<String, Object> getBindResource() {
		return bindResource;
	}

	private void setBindResource(Map<String, Object> resource) {
		if (resource == null) {
			this.bindResource = new HashMap<>();
		} else {
			this.bindResource = resource;
		}
	}

	public String getSyslogDrainUrl() {
		return syslogDrainUrl;
	}

	public String getAppGuid() {
		return appGuid;
	}

}