package org.servicebroker.routeservice.config;

import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Created by Cher on 11/06/2017.
 */
@Configuration
@Profile("cloud")
public class CloudConfig extends AbstractCloudConfig {
}


