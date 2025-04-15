package org.qubership.integration.platform.sessions.configuration.tenant;

import org.qubership.cloud.maas.client.impl.http.HttpClient;
import org.qubership.cloud.security.core.auth.M2MManager;
import org.qubership.cloud.tenantmanager.client.TenantManagerConnector;
import org.qubership.cloud.tenantmanager.client.impl.TenantManagerConnectorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class TenantManagerConfiguration {
    @Bean
    @Profile("!development")
    TenantManagerConnector tenantManagerConnector(@Autowired M2MManager m2MManager) {
        HttpClient httpClient = new HttpClient(() -> m2MManager.getToken().getTokenValue());
        return new TenantManagerConnectorImpl(httpClient);
    }
}
