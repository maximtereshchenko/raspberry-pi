package com.github.maximtereshchenko.tutorial;

import com.diozero.api.GpioPullUpDown;
import com.diozero.api.SmoothedInputDevice;
import com.diozero.devices.RgbLed;
import com.diozero.util.DiozeroScheduler;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

final class Discolor {

    public static void main(String[] args) throws InterruptedException {
        try (
            var touchSensor = new SmoothedInputDevice(18, GpioPullUpDown.PULL_DOWN, 5, 10, 20);
            var led = new RgbLed(22, 27, 17)
        ) {
            led.setValues(true, true, true);
            var counter = 1;
            var deadline = Instant.now().plusSeconds(30);
            while (Instant.now().isBefore(deadline)) {
                if (touchSensor.isActive()) {
                    switch (counter++) {
                        case 0 -> led.setValues(true, true, true);
                        case 1 -> led.setValues(false, true, true);
                        case 2 -> led.setValues(true, false, true);
                        case 3 -> {
                            led.setValues(true, true, false);
                            counter = 0;
                        }
                    }
                    TimeUnit.MILLISECONDS.sleep(500);
                }
            }
        } finally {
            DiozeroScheduler.shutdownAll();
        }
    }
}
