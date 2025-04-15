package org.qubership.integration.platform.sessions.logging.constant;

import java.util.Map;

public final class BusinessIds {
    /**
     * [request_prop_name, log_prop_name]
     * Mappings for businessIdentifiers map building
     */
    public static final Map<String, String> MAPPING = Map.of(
            "sessionId", "sessionId",
            "elementId", "sessionElementId",
            "chainId", "chainId"
    );

    public static final String BUSINESS_IDS = "businessIdentifiers";

    private BusinessIds() {
    }
}
