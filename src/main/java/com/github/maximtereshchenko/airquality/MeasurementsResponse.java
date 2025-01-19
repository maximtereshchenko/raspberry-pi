package com.github.maximtereshchenko.airquality;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
record MeasurementsResponse(List<Measurement> list) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Measurement(Main main, Components components, @JsonProperty("dt") Instant instant) {}

    record Main(@JsonProperty("aqi") int airQualityIndex) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Components(
        @JsonProperty("pm2_5")
        double fineParticlesMatter25,
        @JsonProperty("no2")
        double nitrogenDioxide,
        @JsonProperty("co")
        double carbonMonoxide
    ) {}
}
