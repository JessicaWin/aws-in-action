package com.jessica.social.network.serverless.item;

import org.apache.commons.lang3.StringUtils;

public abstract class BasicItem {
    private final static String SEPARATOR = "#";
    public abstract String getHashKey();
    public abstract void setHashKey(String hashKey);
    public abstract String getRangeKey();
    public abstract void setRangeKey(String rangeKey);
    public String getUniqueKey() {
        return StringUtils.joinWith(SEPARATOR, this.getHashKey(), this.getRangeKey());
    }
}
