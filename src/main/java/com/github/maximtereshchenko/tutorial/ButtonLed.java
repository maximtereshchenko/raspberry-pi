package com.github.maximtereshchenko.tutorial;

import com.diozero.devices.Button;
import com.diozero.devices.LED;

import java.time.Instant;

final class ButtonLed {

    public static void main(String[] args) {
        var deadline = Instant.now().plusSeconds(30);
        try (var led = new LED(17); var button = new Button(18)) {
            while (Instant.now().isBefore(deadline)) {
                if (button.isActive()) {
                    led.off();
                } else {
                    led.on();
                }
            }
        }
    }
}
