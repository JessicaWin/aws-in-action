package com.jessica.social.network.serverless.xray.filter;

import java.util.Set;

public interface XRayFilterConfig {
    /**
     * @return
     */
    Set<String> getUrlPatterns();

    /**
     * @param enabled
     */
    void setFilterEnabled(boolean enabled);

    /**
     * @return
     */
    boolean isFilterEnabled();
}
