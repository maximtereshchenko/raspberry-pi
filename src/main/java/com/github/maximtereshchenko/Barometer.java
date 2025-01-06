package com.github.maximtereshchenko;

import com.diozero.devices.BMP180;

import java.time.Instant;

final class Barometer {

    public static void main(String[] args) {
        try (var barometer = new BMP180(BMP180.Mode.STANDARD)) {
            barometer.readCalibrationData();
            var deadline = Instant.now().plusSeconds(15);
            while (Instant.now().isBefore(deadline)) {
                System.out.printf(
                    "Temperature: %.2fC%nPressure: %.2fhPa%nAltitude: %.2fm%n%n",
                    barometer.getTemperature(),
                    barometer.getPressure(),
                    barometer.calculateAltitude(1013.25f, 15)
                );
            }
        }
    }
}
