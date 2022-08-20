package com.robdev.tradeprocessor.enrichment;

import java.util.HashMap;
import java.util.List;
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
        "tradeID",
        "tradingGuid",
        "matched",
        "nominalValue",
        "instrumentGuid",
        "instrument",
        "executionDateTime",
        "reports"
})
@Generated("jsonschema2pojo")
public class Trade {

    @JsonProperty("_id")
    private String id;
    @JsonProperty("tradeID")
    private Integer tradeID;
    @JsonProperty("tradingGuid")
    private String tradingGuid;
    @JsonProperty("matched")
    private Boolean matched;
    @JsonProperty("nominalValue")
    private String nominalValue;
    @JsonProperty("instrumentGuid")
    private String instrumentGuid;
    @JsonProperty("instrument")
    private String instrument;
    @JsonProperty("executionDateTime")
    private String executionDateTime;
    @JsonProperty("reports")
    private List<Report> reports = null;
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

    @JsonProperty("tradeID")
    public Integer getTradeID() {
        return tradeID;
    }

    @JsonProperty("tradeID")
    public void setTradeID(Integer tradeID) {
        this.tradeID = tradeID;
    }

    @JsonProperty("tradingGuid")
    public String getTradingGuid() {
        return tradingGuid;
    }

    @JsonProperty("tradingGuid")
    public void setTradingGuid(String tradingGuid) {
        this.tradingGuid = tradingGuid;
    }

    @JsonProperty("matched")
    public Boolean getMatched() {
        return matched;
    }

    @JsonProperty("matched")
    public void setMatched(Boolean matched) {
        this.matched = matched;
    }

    @JsonProperty("nominalValue")
    public String getNominalValue() {
        return nominalValue;
    }

    @JsonProperty("nominalValue")
    public void setNominalValue(String nominalValue) {
        this.nominalValue = nominalValue;
    }

    @JsonProperty("instrumentGuid")
    public String getInstrumentGuid() {
        return instrumentGuid;
    }

    @JsonProperty("instrumentGuid")
    public void setInstrumentGuid(String instrumentGuid) {
        this.instrumentGuid = instrumentGuid;
    }

    @JsonProperty("instrument")
    public String getInstrument() {
        return instrument;
    }

    @JsonProperty("instrument")
    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    @JsonProperty("executionDateTime")
    public String getExecutionDateTime() {
        return executionDateTime;
    }

    @JsonProperty("executionDateTime")
    public void setExecutionDateTime(String executionDateTime) {
        this.executionDateTime = executionDateTime;
    }

    @JsonProperty("reports")
    public List<Report> getReports() {
        return reports;
    }

    @JsonProperty("reports")
    public void setReports(List<Report> reports) {
        this.reports = reports;
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