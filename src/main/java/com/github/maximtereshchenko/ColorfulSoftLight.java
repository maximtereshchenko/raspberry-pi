package com.github.maximtereshchenko;

import com.diozero.devices.RgbPwmLed;

import java.time.Instant;

final class ColorfulSoftLight {

    public static void main(String[] args) {
        var adc = new ADS7830Device();
        try (var led = new RgbPwmLed(17, 27, 22)) {
            var deadline = Instant.now().plusSeconds(30);
            while (Instant.now().isBefore(deadline)) {
                led.setValues(
                    dutyCycle(adc.analogRead(0)),
                    dutyCycle(adc.analogRead(1)),
                    dutyCycle(adc.analogRead(2))
                );
            }
        }
    }

    private static float dutyCycle(int value) {
        return value / 255.0f;
    }
}
