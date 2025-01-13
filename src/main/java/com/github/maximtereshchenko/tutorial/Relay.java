package com.github.maximtereshchenko.tutorial;

import com.diozero.api.GpioPullUpDown;
import com.diozero.devices.Button;
import com.diozero.devices.LED;

import java.util.concurrent.TimeUnit;

final class Relay {

    public static void main(String[] args) throws InterruptedException {
        try (
            var button = new Button(18, GpioPullUpDown.PULL_UP);
            var led = new LED(17)
        ) {
            button.whenPressed(time -> led.toggle());
            TimeUnit.SECONDS.sleep(30);
        }
    }
}
