package com.github.maximtereshchenko.tutorial;

import com.diozero.api.GpioPullUpDown;
import com.diozero.devices.Button;
import com.github.maximtereshchenko.device.ADS7830Device;

import java.time.Instant;

final class Joystick {

    public static void main(String[] args) {
        var adc = new ADS7830Device();
        try (var button = new Button(18, GpioPullUpDown.PULL_UP)) {
            var deadline = Instant.now().plusSeconds(30);
            while (Instant.now().isBefore(deadline)) {
                System.out.printf(
                    "%d x %d y %b z%n",
                    adc.analogRead(1),
                    adc.analogRead(0),
                    button.isPressed()
                );
            }
        }
    }
}
