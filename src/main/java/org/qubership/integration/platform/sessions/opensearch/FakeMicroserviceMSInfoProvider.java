package org.qubership.integration.platform.sessions.opensearch;

import lombok.Getter;
import org.qubership.cloud.dbaas.client.config.DefaultMSInfoProvider;
import org.springframework.beans.factory.annotation.Value;

/**
 * Replaces the name of the microservice to custom name
 * Useful when accessing the same database via DBaaS from another microservices
 */
public class FakeMicroserviceMSInfoProvider extends DefaultMSInfoProvider {
    @Getter
    @Value("${qip.internal-services.engine}")
    private String microserviceName;

}
