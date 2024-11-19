package com.github.maximtereshchenko;

import com.diozero.devices.PwmLed;

import java.time.Instant;

final class SoftLight {

    public static void main(String[] args) {
        try (
            var led = new PwmLed(17);
            var adc = new ADS7830Device()
        ) {
            var deadline = Instant.now().plusSeconds(30);
            while (Instant.now().isBefore(deadline)) {
                led.setValue(dutyCycle(adc.analogRead(0)));
            }
        }
    }

    private static float dutyCycle(int value) {
        return value / 255.0f;
    }
}
