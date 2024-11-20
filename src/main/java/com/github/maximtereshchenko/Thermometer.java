package com.github.maximtereshchenko;

import java.time.Instant;

final class Thermometer {

    public static void main(String[] args) {
        var adc = new ADS7830Device();
        var deadline = Instant.now().plusSeconds(30);
        while (Instant.now().isBefore(deadline)) {
            var totalVoltage = 3.3;
            var zeroCelsiusKelvins = 273.15;
            var nominalResistance = 10000;
            var nominalTemperatureCelsius = 25;
            var betaValue = 3950;
            var voltage = adc.analogRead(0) / 255.0 * totalVoltage;
            var resistance = voltage * nominalResistance / (totalVoltage - voltage); //resistance divider rule
            var kelvins = 1 / (1 / (zeroCelsiusKelvins + nominalTemperatureCelsius) + Math.log(resistance / nominalResistance) / betaValue);
            System.out.println(kelvins - zeroCelsiusKelvins);
        }
    }
}
