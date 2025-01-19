package com.github.maximtereshchenko.airquality;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Clock;
import java.time.LocalTime;
import java.util.List;

final class OpenWeather {

    private final String apiKey;
    private final String cityName;
    private final Clock clock;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    OpenWeather(String apiKey, String cityName, Clock clock) {
        this.apiKey = apiKey;
        this.cityName = cityName;
        this.clock = clock;
    }

    AirQuality airQuality() throws IOException, InterruptedException {
        return airQuality(measurements(city()));
    }

    private AirQuality airQuality(MeasurementsResponse response) {
        var measurement = response.list().getFirst();
        var components = measurement.components();
        return new AirQuality(
            LocalTime.ofInstant(measurement.instant(), clock.getZone()),
            components.fineParticlesMatter25(),
            components.nitrogenDioxide(),
            components.carbonMonoxide()
        );
    }

    private City city() throws IOException, InterruptedException {
        return httpClient.send(
                HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("http://api.openweathermap.org/geo/1.0/direct?q=%s&appid=%s".formatted(cityName, apiKey)))
                    .build(),
                new JsonBodyHandler<>(new TypeReference<List<City>>() {})
            )
            .body()
            .getFirst();
    }

    private MeasurementsResponse measurements(City city) throws IOException, InterruptedException {
        return httpClient.send(
                HttpRequest.newBuilder()
                    .GET()
                    .uri(
                        URI.create(
                            "http://api.openweathermap.org/data/2.5/air_pollution?lat=%f&lon=%f&appid=%s"
                                .formatted(city.latitude(), city.longitude(), apiKey)
                        )
                    )
                    .build(),
                new JsonBodyHandler<>(new TypeReference<MeasurementsResponse>() {})
            )
            .body();
    }
}
