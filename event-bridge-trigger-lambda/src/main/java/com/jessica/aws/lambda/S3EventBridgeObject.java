package com.jessica.aws.lambda;

import java.util.ArrayList;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class S3EventBridgeObject {
    private String id;
    private String version;
    @JsonFilter(value = "detail-type")
    private String detailType;
    private String source;
    private String account;
    private String time;
    private String region;
    private ArrayList<String> resources;
    private S3EventDetail detail;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class S3EventDetail {
        private String version;
        private S3EventBucketName bucket;
        private S3EventBucketObject object;
        @JSONField(name = "request-id")
        private String requestId;
        private String requester;
        @JSONField(name = "source-ip-address")
        private String sourceIpAddress;
        private String reason;
        @JSONField(name = "deletion-type")
        private String deletionType;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class S3EventBucketName {
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class S3EventBucketObject {
        private String key;
        private int size;
        private String etag;
        @JSONField(name = "version-id")
        private String versionId;
        private String sequencer;
    }

}
