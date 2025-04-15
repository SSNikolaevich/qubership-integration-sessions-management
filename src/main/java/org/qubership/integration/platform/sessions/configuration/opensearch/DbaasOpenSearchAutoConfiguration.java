/*
 * Copyright 2024-2025 NetCracker Technology Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.qubership.integration.platform.sessions.configuration.opensearch;

import org.qubership.cloud.dbaas.client.config.MSInfoProvider;
import org.qubership.cloud.dbaas.client.entity.database.DatabaseSettings;
import org.qubership.cloud.dbaas.client.management.DatabaseConfig;
import org.qubership.cloud.dbaas.client.management.DatabasePool;
import org.qubership.cloud.dbaas.client.management.classifier.DbaasClassifierFactory;
import org.qubership.cloud.dbaas.client.opensearch.DbaasOpensearchClient;
import org.qubership.cloud.dbaas.client.opensearch.DbaasOpensearchClientImpl;
import org.qubership.cloud.dbaas.client.opensearch.config.EnableTenantDbaasOpensearch;
import org.qubership.cloud.dbaas.client.opensearch.config.OpensearchConfig;
import org.qubership.cloud.dbaas.client.opensearch.entity.OpensearchDatabaseSettings;
import org.qubership.cloud.dbaas.client.opensearch.entity.OpensearchProperties;
import org.qubership.integration.platform.sessions.opensearch.DbaasOpenSearchClientSupplier;
import org.qubership.integration.platform.sessions.opensearch.FakeMicroserviceMSInfoProvider;
import org.qubership.integration.platform.sessions.opensearch.OpenSearchClientSupplier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.Collections;

import static org.qubership.cloud.dbaas.client.DbaasConst.LOGICAL_DB_NAME;
import static org.qubership.cloud.dbaas.client.opensearch.config.DbaasOpensearchConfiguration.TENANT_NATIVE_OPENSEARCH_CLIENT;

@AutoConfiguration
@EnableTenantDbaasOpensearch
@ConditionalOnProperty(prefix = "dbaas", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DbaasOpenSearchAutoConfiguration {
    @Bean
    public OpenSearchClientSupplier openSearchClientSupplier(DbaasOpensearchClient client) {
        return new DbaasOpenSearchClientSupplier(client);
    }

    @Bean(TENANT_NATIVE_OPENSEARCH_CLIENT)
    public DbaasOpensearchClient opensearchClient(
            DatabasePool dbaasConnectionPool,
            DbaasClassifierFactory classifierFactory,
            OpensearchProperties opensearchProperties
    ) {
        DatabaseSettings dbSettings = getDatabaseSettings();
        DatabaseConfig.Builder databaseConfigBuilder = DatabaseConfig.builder()
                .userRole(opensearchProperties.getRuntimeUserRole())
                .databaseSettings(dbSettings);
        OpensearchConfig opensearchConfig = new OpensearchConfig(opensearchProperties, opensearchProperties.getTenant().getDelimiter());
        return new DbaasOpensearchClientImpl(dbaasConnectionPool,
                classifierFactory.newTenantClassifierBuilder().withCustomKey(LOGICAL_DB_NAME, "sessions"), databaseConfigBuilder, opensearchConfig);
    }

    @Bean
    @Primary
    public MSInfoProvider fakeMicroserviceMSInfoProvider() {
        return new FakeMicroserviceMSInfoProvider();
    }

    private DatabaseSettings getDatabaseSettings() {
        OpensearchDatabaseSettings opensearchDatabaseSettings = new OpensearchDatabaseSettings();
        opensearchDatabaseSettings.setResourcePrefix(true);
        opensearchDatabaseSettings.setCreateOnly(Collections.singletonList("user"));
        return opensearchDatabaseSettings;
    }
}
