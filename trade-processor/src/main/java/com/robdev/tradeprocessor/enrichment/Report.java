package com.robdev.tradeprocessor.enrichment;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "_id",
        "timestamp",
        "firmId",
        "institutionId"
})
@Generated("jsonschema2pojo")
public class Report {

    @JsonProperty("_id")
    private String id;
    @JsonProperty("timestamp")
    private Integer timestamp;
    @JsonProperty("firmId")
    private Integer firmId;
    @JsonProperty("institutionId")
    private Integer institutionId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("timestamp")
    public Integer getTimestamp() {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty("firmId")
    public Integer getFirmId() {
        return firmId;
    }

    @JsonProperty("firmId")
    public void setFirmId(Integer firmId) {
        this.firmId = firmId;
    }

    @JsonProperty("institutionId")
    public Integer getInstitutionId() {
        return institutionId;
    }

    @JsonProperty("institutionId")
    public void setInstitutionId(Integer institutionId) {
        this.institutionId = institutionId;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}