package org.qubership.integration.platform.sessions.opensearch;

import org.opensearch.client.opensearch.OpenSearchClient;
import org.qubership.cloud.dbaas.client.opensearch.DbaasOpensearchClient;

public class DbaasOpenSearchClientSupplier implements OpenSearchClientSupplier {
    private final DbaasOpensearchClient client;

    public DbaasOpenSearchClientSupplier(DbaasOpensearchClient client) {
        this.client = client;
    }

    @Override
    public OpenSearchClient getClient() {
        return client.getClient();
    }

    @Override
    public String normalize(String name) {
        return client.normalize(name);
    }
}

