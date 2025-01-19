package com.github.maximtereshchenko.airquality;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;
import java.util.stream.Stream;

final class Application {

    private final OpenWeather openWeather;
    private final Display display;
    private final Indicator fineParticlesMatter25Indicator;
    private final Indicator nitrogenDioxideIndicator;
    private final Indicator carbonMonoxideIndicator;

    Application(
        OpenWeather openWeather,
        Display display,
        Indicator fineParticlesMatter25Indicator,
        Indicator nitrogenDioxideIndicator,
        Indicator carbonMonoxideIndicator
    ) {
        this.openWeather = openWeather;
        this.display = display;
        this.fineParticlesMatter25Indicator = fineParticlesMatter25Indicator;
        this.nitrogenDioxideIndicator = nitrogenDioxideIndicator;
        this.carbonMonoxideIndicator = carbonMonoxideIndicator;
    }

    void turnOff() {
        try {
            display.turnOff();
            Stream.of(fineParticlesMatter25Indicator, nitrogenDioxideIndicator, carbonMonoxideIndicator).forEach(Indicator::turnOff);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    void displayAirQuality() {
        try {
            var airQuality = openWeather.airQuality();
            display.write(airQuality);
            lightUp(
                fineParticlesMatter25Indicator,
                levels(10, 25, 50, 75),
                airQuality.fineParticlesMatter25()
            );
            lightUp(
                nitrogenDioxideIndicator,
                levels(40, 70, 150, 200),
                airQuality.nitrogenDioxide()
            );
            lightUp(
                carbonMonoxideIndicator,
                levels(4400, 9400, 12400, 15400),
                airQuality.carbonMonoxide()
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Map<Integer, Level> levels(int good, int fair, int moderate, int poor) {
        return Map.of(
            good, Level.GOOD,
            fair, Level.FAIR,
            moderate, Level.MODERATE,
            poor, Level.POOR,
            Integer.MAX_VALUE, Level.VERY_POOR
        );
    }

    private void lightUp(Indicator indicator, Map<Integer, Level> levels, double actual) {
        indicator.lightUp(
            levels.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .filter(entry -> actual < entry.getKey())
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseThrow()
        );
    }
}
