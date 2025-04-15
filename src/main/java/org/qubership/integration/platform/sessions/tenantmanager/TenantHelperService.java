package org.qubership.integration.platform.sessions.tenantmanager;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.qubership.cloud.framework.contexts.tenant.context.TenantContext;
import org.qubership.cloud.tenantmanager.client.Tenant;
import org.qubership.cloud.tenantmanager.client.TenantManagerConnector;
import org.qubership.integration.platform.sessions.configuration.tenant.TenantConfiguration;
import org.qubership.integration.platform.sessions.utils.DevModeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Component
public class TenantHelperService {
    private final TenantManagerConnector tenantManagerConnector;
    private final TenantConfiguration tenantConfiguration;
    private final DevModeUtil devModeUtil;
    @Getter
    private final String defaultTenant;

    @Autowired
    public TenantHelperService(@Autowired(required = false) TenantManagerConnector tenantManagerConnector,
                               TenantConfiguration tenantConfiguration,
                               DevModeUtil devModeUtil) {
        this.tenantManagerConnector = tenantManagerConnector;
        this.tenantConfiguration = tenantConfiguration;
        this.devModeUtil = devModeUtil;
        this.defaultTenant = tenantConfiguration.getDefaultTenant();
    }

    /**
     * Requires no transactions (existing or already closed) in the current thread
     */
    public void invokeForAllTenants(Consumer<String> callback) {
        invokeForAllTenants(callback, callback);
    }

    /**
     * Requires no transactions (existing or already closed) in the current thread
     */
    public void invokeForAllTenants(Consumer<String> tenantsCallback, Consumer<String> devmodeCallback) {
        if (!devModeUtil.isDevMode()) {
            if (tenantManagerConnector != null) {
                List<Tenant> tenantList = tenantManagerConnector.getTenantList();
                log.debug("Available tenants: {}", tenantList);
                for (Tenant tenant : tenantList) {
                    TenantContext.set(tenant.getExternalId());
                    tenantsCallback.accept(TenantContext.get());
                }
            } else {
                log.error("Failed to invoke callback for all tenants. TenantManagerConnector bean not present");
            }
        } else {
            TenantContext.set(tenantConfiguration.getDefaultTenant());
            devmodeCallback.accept(TenantContext.get());
        }
    }
}
