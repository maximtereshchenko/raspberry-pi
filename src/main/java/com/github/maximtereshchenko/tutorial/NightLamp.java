package com.github.maximtereshchenko.tutorial;

import com.diozero.devices.PwmLed;
import com.github.maximtereshchenko.device.ADS7830Device;

import java.time.Instant;

final class NightLamp {

    public static void main(String[] args) {
        var adc = new ADS7830Device();
        try (var led = new PwmLed(17)) {
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
