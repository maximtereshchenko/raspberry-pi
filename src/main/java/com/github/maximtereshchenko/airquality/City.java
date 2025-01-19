package com.github.maximtereshchenko.airquality;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
record City(@JsonProperty("lat") double latitude, @JsonProperty("lon") double longitude) {}
