package com.github.maximtereshchenko.tutorial;

import com.diozero.api.DigitalInputDevice;
import com.diozero.devices.LED;

import java.time.Instant;

final class SenseLed {

    public static void main(String[] args) {
        try (var led = new LED(18); var sensor = new DigitalInputDevice(17)) {
            var deadline = Instant.now().plusSeconds(15);
            while (Instant.now().isBefore(deadline)) {
                led.setValue(sensor.isActive());
            }
        }
    }
}
